plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.convention.buildkonfig)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)

            implementation(libs.bundles.koin.common)

            implementation(libs.bundles.ktor.common)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kermit)

            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.coroutines)
            implementation(libs.multiplatform.settings.no.arg)
        }

        getByName("mobileMain").dependencies {
            implementation(libs.datastore)
            implementation(libs.datastore.preferences)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        getByName("jsMain").dependencies {
            implementation(libs.ktor.client.js)
        }

        getByName("wasmJsMain").dependencies {
            implementation(libs.ktor.client.js)
        }
    }
}
