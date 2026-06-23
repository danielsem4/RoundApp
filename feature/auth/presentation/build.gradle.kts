plugins {
    alias(libs.plugins.convention.cmp.feature)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "org.example.roundapp.feature.auth.presentation.resources"
    generateResClass = always
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.feature.auth.domain)
        }
    }
}
