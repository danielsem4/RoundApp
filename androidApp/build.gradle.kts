import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

// The new com.android.kotlin.multiplatform.library plugin doesn't bundle each KMP
// module's compose resources into its AAR's assets, so they never reach this APK.
// Aggregate them here: each module's preparedResources/commonMain output gets wrapped
// under its packageOfResClass directory so the runtime ResourceReader can find them
// at assets/composeResources/<package>/<sourceset>/...
val composeResourceModules = mapOf(
    ":core:designsystem" to "org.example.roundapp.core.designsystem.resources",
    ":feature:auth:presentation" to "org.example.roundapp.feature.auth.presentation.resources",
)

val composeAndroidAssetsDir = layout.buildDirectory.dir("generated/composeAndroidAssets")

val aggregateComposeAndroidResources by tasks.registering(Sync::class) {
    destinationDir = composeAndroidAssetsDir.get().asFile
    composeResourceModules.forEach { (modulePath, pkg) ->
        val module = project(modulePath)
        dependsOn("$modulePath:prepareComposeResourcesTaskForCommonMain")
        from(
            module.layout.buildDirectory.dir(
                "generated/compose/resourceGenerator/preparedResources/commonMain/composeResources"
            )
        ) {
            into("composeResources/$pkg")
        }
    }
}

android {
    namespace = libs.versions.projectApplicationId.get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.projectApplicationId.get()
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.projectVersionCode.get().toInt()
        versionName = libs.versions.projectVersionName.get()
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    sourceSets["main"].assets.srcDirs(composeAndroidAssetsDir.get().asFile)
}

tasks.matching {
    it.name == "mergeDebugAssets" ||
        it.name == "mergeReleaseAssets" ||
        it.name == "packageDebugAssets" ||
        it.name == "packageReleaseAssets"
}.configureEach {
    dependsOn(aggregateComposeAndroidResources)
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.compose.components.resources)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)

    debugImplementation(libs.compose.uiTooling)
    debugImplementation(libs.compose.uiToolingPreview)
}
