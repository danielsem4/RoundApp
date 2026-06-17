package org.example.roundapp.convention

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureKotlinMultiplatform() {
    extensions.configure<KotlinMultiplatformExtension> {
        (this as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryExtension>("androidLibrary") {
            namespace = this@configureKotlinMultiplatform.pathToPackageName()
            compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()
            minSdk = libs.findVersion("projectMinSdkVersion").get().toString().toInt()
            withHostTest { }
        }

        listOf(
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = this@configureKotlinMultiplatform.pathToFrameworkName()
                isStatic = true
            }
        }

        js {
            browser()
        }

        @OptIn(ExperimentalWasmDsl::class)
        wasmJs {
            browser()
        }

        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
        }

        applyDefaultHierarchyTemplate()

        // Default hierarchy in Kotlin 2.4 already provides webMain (js + wasmJs).
        // Add a custom `mobileMain` group covering android + iOS so libraries without web
        // support (Room, DataStore, Firebase, Moko Permissions) can live there safely.
        sourceSets.apply {
            val commonMain = getByName("commonMain")
            val mobileMain = maybeCreate("mobileMain").apply { dependsOn(commonMain) }
            getByName("androidMain").dependsOn(mobileMain)
            getByName("iosMain").dependsOn(mobileMain)
        }
    }
}
