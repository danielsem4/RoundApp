# RoundApp — Architecture & Build Rules

This file is loaded automatically by Claude Code in every session. Follow these rules when adding, editing, or refactoring any module. They describe the conventions already in place in this codebase; the cited files are the canonical examples.

Kotlin Multiplatform project. Targets: Android, iOS, Web (wasmJs + js). UI: Compose Multiplatform. Root package: `org.example.roundapp`.

---

## 1. Architecture

Clean Architecture with MVVM/MVI. Every feature module has up to three layers, each a separate Gradle module:

- `domain/` — interfaces, models, use cases. **No framework dependencies.** Pure Kotlin only — no `androidx.*`, no `io.ktor.*`, no `androidx.compose.*`.
- `data/` — Ktor service implementations, DTOs, mappers. Depends on `domain`.
- `presentation/` — ViewModels, Compose UI, navigation routes, State/Action/Event classes. Depends on `domain` (**NOT** on `data`).

**Dependency rule:** `presentation` and `data` both depend on `domain`. `presentation` MUST NOT depend on `data` — wire implementations through Koin instead.

Add only the layers a feature actually needs. `feature/auth` currently has `domain` + `presentation` only; that's fine until it grows a backend.

---

## 2. Module Groups

- `core/` — shared foundation across all features:
  - `core/domain` — `Result`, `DataError`, `AuthService`, `SessionStorage`, shared models.
  - `core/data` — Ktor `HttpClientFactory`, `KtorAuthService`, `DataStoreSessionStorage` (mobile) / `SettingsSessionStorage` (web), `KermitLogger`.
  - `core/presentation` — `UiText`, `ObserveAsEvents`, base presentation helpers.
  - `core/designsystem` — `AppTheme` and design tokens.
- `feature/<name>/{domain,data,presentation}` — one directory per feature.

Do not invent new top-level groups speculatively. Add one (e.g. `sensors/`) only when justified by cross-feature hardware/platform concerns.

---

## 3. Key Patterns

### DI (Koin)
- Each module defines a Koin module in its `di/` package, suffixed `Module.kt` (e.g. `HomePresentationModule.kt`, `HomeDataModule.kt`, `CoreDataModule.kt`).
- All modules are aggregated in `composeApp/src/commonMain/kotlin/org/example/roundapp/di/initKoin.kt` (via `AppModule.kt`).
- Conventions:
  - `viewModelOf(::Foo)` for ViewModels.
  - `singleOf(::Bar) bind IBar::class` for service implementations that hold state or resources (HTTP client, session storage).
  - `factoryOf(::Baz)` for use cases (stateless).
- Platform-specific bindings use `expect`/`actual`: declare `expect fun Module.platformX()` in `commonMain/di/`, implement in `androidMain/di/CoreDataModule.android.kt`, `iosMain/di/CoreDataModule.ios.kt`, `webMain/di/CoreDataModule.web.kt` (see `core/data/.../di/`).

### Navigation
- Jetpack Compose Navigation with **type-safe `@Serializable` route objects**.
- Each feature defines route objects + a graph extension in `feature/<name>/presentation/navigation/`:
  ```kotlin
  object HomeGraphRoutes {
      @Serializable data object Graph
      @Serializable data object Home
  }
  fun NavGraphBuilder.homeGraph(onLogout: () -> Unit) { ... }
  ```
- All graphs are composed in `composeApp/.../navigation/NavigationRoot.kt`. New features must register their graph there.

### Networking
- Ktor client configured in `core/data/.../networking/HttpClientFactory.kt`.
- Services call extension helpers from `HttpClientExt.kt` — wrap requests in `safeCall { ... }` so exceptions become `Result.Failure(DataError.Remote.*)`.
- Base URL in `UrlConstants.kt`.
- Engine selection is `expect`/`actual`: `HttpClientExt.android.kt`, `.ios.kt`, `.web.kt`.

### Error Handling
- Use `Result<D, E>` (`core/domain/util/Result.kt`) with `Success` / `Failure`. Combinators available: `map`, `onSuccess`, `onFailure`, `asEmptyResult`.
- Errors are `DataError.Remote` (HTTP states: `UNAUTHORIZED`, `NOT_FOUND`, `NO_INTERNET`, `SERVER_ERROR`, `SERIALIZATION_ERROR`, etc.) or `DataError.Local` (`DISK_FULL`, `NOT_FOUND`, `UNKNOWN`).
- **Never throw across layer boundaries.** Services always return `Result`. The data layer is the only place that catches exceptions.
- Map errors to user-facing strings via `UiText` (`core/presentation/ui/UiText.kt`) — never hard-code user-facing strings in ViewModels.

### ViewModel Pattern
Extends `androidx.lifecycle.ViewModel`. Canonical shape (see `HomeViewModel.kt`):

```kotlin
class FooViewModel(private val service: FooService) : ViewModel() {
    private val _state = MutableStateFlow(FooState())
    val state: StateFlow<FooState> = _state.asStateFlow()

    private val _events = Channel<FooEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: FooAction) { when (action) { ... } }
}
```

- One entry point: `fun onAction(action: FooAction)` over a sealed `FooAction`.
- State mutations: `_state.update { it.copy(...) }`.
- Side effects launched in `viewModelScope`.
- One-shot UI signals (navigation, toast, logout) go through `Channel`/`receiveAsFlow`, never State.

### Screen Composition
- Two-Composable pattern per screen:
  - **Stateful root**: `FooScreenRoot(...)` — hoists `koinViewModel()`, collects state, dispatches actions, observes events. Connected to navigation callbacks.
  - **Stateless body**: `FooScreen(state, onAction)` — takes plain `FooState` + `(FooAction) -> Unit`. Previewable, testable, no Koin/ViewModel dependencies.
- See `HomeScreen.kt` for the canonical example.

### Platform Differences
- Use `expect`/`actual`. The `commonMain` file has no suffix; platform files use `.android.kt`, `.ios.kt`, `.web.kt`, `.wasmJs.kt`, `.js.kt`.
- Existing platform-split surfaces: Ktor engine, session storage (DataStore vs Multiplatform Settings), Firebase services.

---

## 4. Convention Plugins

Applied as `alias(libs.plugins.convention.<name>)` in module `build.gradle.kts`. Aliases live in `gradle/libs.versions.toml`:

| Plugin | Use for |
|---|---|
| `convention.cmp.application` | `composeApp` only |
| `convention.cmp.feature` | feature `presentation` and `data` modules (Koin + Compose deps included) |
| `convention.cmp.library` | Compose library modules (`core/designsystem`, `core/presentation`) |
| `convention.kmp.library` | non-Compose KMP libraries (`core/domain`, feature `domain` modules) |
| `convention.android.application` / `.compose` | Android-only application modules |
| `convention.room` | Room DB modules |
| `convention.buildkonfig` | build-time config |

`projectApplicationId` and the derived package root are set in `libs.versions.toml` (`org.example.roundapp`). All Kotlin packages start with that prefix.

---

## 5. Adding a New Feature Module

When adding feature `<name>`:

1. Create the directories the feature needs. Start with `feature/<name>/domain/` + `feature/<name>/presentation/`. Add `feature/<name>/data/` only if it owns its own service.
2. Add `build.gradle.kts` to each:
   - `domain` → `convention.kmp.library`
   - `data` → `convention.cmp.feature`
   - `presentation` → `convention.cmp.feature`
   - The `presentation` module should `implementation(projects.feature.<name>.domain)` — never `data`.
   - The `data` module should `implementation(projects.feature.<name>.domain)` (+ `core.data`).
3. Register each module in `settings.gradle.kts` (`include(":feature:<name>:domain")`, etc.).
4. Add a Koin module per layer in `di/` (`<Name>DomainModule.kt`, `<Name>DataModule.kt`, `<Name>PresentationModule.kt`).
5. Wire the new Koin modules into `composeApp/.../di/AppModule.kt` (`allModules` list, consumed by `initKoin.kt`).
6. Define route objects + graph extension in `feature/<name>/presentation/navigation/<Name>Graph.kt`.
7. Register the graph in `composeApp/.../navigation/NavigationRoot.kt`.
8. ViewModel + State + Action + Event + ScreenRoot + Screen in `feature/<name>/presentation/<screen>/`.

---

## 6. Naming Conventions

**Packages.** All lowercase. Mirror the Gradle path: `:feature:home:presentation` → `org.example.roundapp.feature.home.presentation`.

**Classes.**
- ViewModels: `<Screen>ViewModel` (e.g. `LoginViewModel`).
- State / Action / Event: `<Screen>State` (data class), `<Screen>Action` (sealed interface), `<Screen>Event` (sealed interface).
- Use cases: `<Verb><Noun>UseCase` (e.g. `LoginUseCase`). One operation per class; expose `suspend operator fun invoke(...)`.
- Service interfaces in `domain`: `<Noun>Service` (e.g. `HomeService`, `AuthService`).
- Service implementations in `data`: prefix with the technology — `Ktor<Noun>Service`, `DataStore<Noun>Storage`, `Stub<Noun>Service` (e.g. `KtorAuthService`, `StubHomeService`, `DataStoreSessionStorage`).
- DTOs in `data/dto/`: `<Noun>Dto` (response shapes), `<Noun>Request`, `<Noun>Response` (e.g. `UserDto`, `LoginRequest`, `AuthResponseDto`).
- Mappers in `data/mapper/`: `<Noun>Mappers.kt` with `toDomain()` / `toDto()` extension functions.
- Koin modules: `<Feature><Layer>Module.kt` (e.g. `HomePresentationModule.kt`, `HomeDataModule.kt`).
- Route objects: `<Feature>GraphRoutes` (object containing nested `@Serializable` data object / data class per destination).

**Composables.** PascalCase, no `Composable` suffix. Screen entry: `<Feature>ScreenRoot` (stateful) + `<Feature>Screen` (stateless).

**expect/actual.** `commonMain` file has no suffix; platform files use `.android.kt`, `.ios.kt`, `.web.kt`, `.wasmJs.kt`, `.js.kt` — matching the existing pattern in `core/data/`.

---

## 7. Kotlin Idioms & Code Style

- **Immutability by default.** State classes are `data class` with `val`-only properties. Mutate via `_state.update { it.copy(...) }`.
- **Sealed interfaces** for closed hierarchies (Action, Event, Error). Prefer `sealed interface` over `sealed class` unless you need shared state.
- **Coroutines.** Launch in `viewModelScope` inside ViewModels. Never block the main thread. Don't wrap Ktor/Room calls in `Dispatchers.IO` — they already manage their own dispatchers. Use `Dispatchers.IO` only for blocking platform APIs (file I/O, etc.).
- **Flows.** Expose `StateFlow<T>` (backed by `MutableStateFlow`) for state. Expose `Flow<T>` from `Channel.receiveAsFlow()` for one-shot events. Never expose `MutableStateFlow` or `Channel` directly.
- **No throws across layer boundaries.** Wrap risky calls in `safeCall { ... }` (returns `Result`). The data layer is the only place that catches exceptions.
- **Domain layer is pure Kotlin.** No `androidx.*`, no `io.ktor.*`, no `androidx.compose.*` imports under `:core:domain` or `:feature:*:domain`.
- **No `lateinit var`** in production code. No mutable global state. No singletons outside Koin.
- **User-facing strings** go through `UiText` so multiplatform UI resolves them. Never hard-code strings in ViewModels.

---

## 8. UI / Design System

### Where reusable UI lives
All reusable presentational components live in `core/designsystem/components/`. Feature `presentation` modules consume them. **Do not redefine equivalents locally in a feature** once a designsystem version exists.

### Dumb components only
Components in `core/designsystem/components/` are stateless:
- Take params only: pure data (`String`, `Boolean`, `Modifier`, value classes, `ImmutableList`) and callbacks (`() -> Unit`, `(T) -> Unit`).
- No `koinViewModel()`, no `ViewModel`, no `Flow.collect`, no `LaunchedEffect` for side effects.
- No business logic, no networking, no navigation calls — render + dispatch callbacks only.
- Must be previewable in isolation.
- Read tokens from `MaterialTheme.{colorScheme, typography, shapes}` — never hard-code colors, dimensions, or shapes.

### When to add a component
Promote a UI element to `core/designsystem/components/` as soon as it:
- is used in 2+ places, OR
- represents an app-level primitive (button, text field, card, dialog, top bar, loading indicator, error banner) that must look consistent everywhere.

Until then, keep it local to the feature's `presentation/` package.

### Naming
Prefix with `App` to distinguish from raw Material3: `AppButton`, `AppTextField`, `AppCard`, `AppTopBar`, `AppDialog`, `AppLoadingIndicator`, `AppErrorBanner`. One component per file: `components/<ComponentName>.kt`.

### Round-first visual language
Prefer rounded geometry over sharp rectangles:
- Use `RoundedCornerShape(...)` for any container with corners (cards, buttons, text fields, dialogs, bottom sheets, snackbars). Default to medium-rounded (~16.dp) for containers and small-rounded (~8.dp) for inline elements. **Avoid `RectangleShape`.**
- Use `CircleShape` for icon buttons, avatars, FABs, badges, and pill-style chips. Prefer fully circular over slightly-rounded for these.
- Avoid `CutCornerShape` unless an explicit design reason justifies it.
- When Material3 offers both a rounded and an angular variant, pick the rounded one (e.g. `FilledIconButton` with `shape = CircleShape`, pill-style `Button` over the default).
- When a concrete shape scale becomes useful, add `core/designsystem/theme/Shapes.kt` and wire it into `MaterialTheme(shapes = ...)` inside `AppTheme.kt` — defer until a real component needs it.

### Standard composable signature
```kotlin
@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    // ...other stateless params
)
```
Always accept `modifier: Modifier = Modifier` so callers can size/position. Don't apply `Modifier.fillMaxWidth()` (or similar layout-claiming modifiers) inside the component unless the component's identity requires it — callers decide layout.

---

## 9. Key Dependencies

Pinned in `gradle/libs.versions.toml` — the single source of truth. Don't add a dependency without updating the catalog.

- Kotlin 2.4.0, Compose Multiplatform 1.11.1, AGP 9.0.1, compileSdk 36, minSdk 24, JVM target 17.
- Ktor 3.2.3 — networking.
- Koin 4.1.0 — DI.
- Room 2.7.2 + SQLite — local DB (mobile only).
- Kotlinx Serialization — JSON + route safety.
- DataStore (mobile) / Multiplatform Settings (web) — persistent KV storage.
- Coil 3 — images.
- Kermit — logging (via `AppLogger` / `KermitLogger`).

---

## 10. Canonical Examples (Open These First)

When in doubt, mirror these files:

- Result / error contract: `core/domain/src/commonMain/kotlin/org/example/roundapp/core/domain/util/Result.kt`, `DataError.kt`.
- Ktor setup: `core/data/src/commonMain/kotlin/org/example/roundapp/core/data/networking/HttpClientFactory.kt`, `HttpClientExt.kt`.
- Service implementation: `core/data/.../auth/KtorAuthService.kt`.
- Multiplatform strings: `core/presentation/.../ui/UiText.kt`.
- ViewModel pattern: `feature/home/presentation/.../HomeViewModel.kt`.
- Screen split: `feature/home/presentation/.../HomeScreen.kt`.
- Feature graph: `feature/home/presentation/.../navigation/HomeGraph.kt`.
- Graph composition: `composeApp/.../navigation/NavigationRoot.kt`.
- DI aggregation: `composeApp/.../di/initKoin.kt` + `AppModule.kt`.
- Version catalog: `gradle/libs.versions.toml`.
