import org.example.roundapp.convention.configureAndroidTarget
import org.example.roundapp.convention.configureIosTargets
import org.example.roundapp.convention.configureWebTargets
import org.gradle.api.Plugin
import org.gradle.api.Project

class CmpApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.example.roundapp.convention.android.application.compose")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            configureAndroidTarget()
            configureIosTargets()
            configureWebTargets(asApp = true)
        }
    }
}
