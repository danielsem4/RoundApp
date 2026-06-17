import org.example.roundapp.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpFeatureConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.example.roundapp.convention.cmp.library")
            }

            dependencies {
                "commonMainImplementation"(project(":core:presentation"))
                "commonMainImplementation"(project(":core:designsystem"))

                "commonMainImplementation"(libs.findLibrary("koin-core").get())
                "commonMainImplementation"(libs.findLibrary("koin-compose").get())
                "commonMainImplementation"(libs.findLibrary("koin-compose-viewmodel").get())

                "commonMainImplementation"(libs.findLibrary("androidx-lifecycle-viewmodelCompose").get())
                "commonMainImplementation"(libs.findLibrary("androidx-lifecycle-runtimeCompose").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-navigation").get())
            }
        }
    }
}
