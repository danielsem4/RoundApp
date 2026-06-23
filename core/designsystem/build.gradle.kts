plugins {
    alias(libs.plugins.convention.cmp.library)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "org.example.roundapp.core.designsystem.resources"
    generateResClass = always
}
