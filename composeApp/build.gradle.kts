plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(projects.core.presentation)
            implementation(projects.core.data)
            implementation(projects.core.domain)

            implementation(projects.feature.auth.domain)
            implementation(projects.feature.auth.presentation)
            implementation(projects.feature.home.domain)
            implementation(projects.feature.home.data)
            implementation(projects.feature.home.presentation)

            implementation(libs.bundles.koin.common)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.jetbrains.compose.navigation)
        }
    }
}
