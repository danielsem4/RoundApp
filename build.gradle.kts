plugins {
    // Declare plugin aliases so versions resolve consistently; modules apply them as needed.
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.buildkonfig) apply false
    // KSP + Room intentionally NOT declared here. They require a KSP build matching the Kotlin
    // version. Re-add and apply convention-room once KSP-for-Kotlin-2.4 is published.
}
