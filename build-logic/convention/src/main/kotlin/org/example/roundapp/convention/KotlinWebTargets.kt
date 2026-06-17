package org.example.roundapp.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureWebTargets(asApp: Boolean = false) {
    extensions.configure<KotlinMultiplatformExtension> {
        js {
            browser()
            if (asApp) binaries.executable()
        }
        @OptIn(ExperimentalWasmDsl::class)
        wasmJs {
            browser()
            if (asApp) binaries.executable()
        }
    }
}
