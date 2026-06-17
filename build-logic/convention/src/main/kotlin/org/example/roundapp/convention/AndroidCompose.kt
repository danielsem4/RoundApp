package org.example.roundapp.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension
) {
    commonExtension.buildFeatures.compose = true

    dependencies {
        "debugImplementation"(libs.findLibrary("compose-uiTooling").get())
        "debugImplementation"(libs.findLibrary("compose-uiToolingPreview").get())
    }
}
