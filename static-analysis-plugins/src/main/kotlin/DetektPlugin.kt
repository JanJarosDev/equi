import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class DetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.plugins.apply("io.gitlab.arturbosch.detekt")
        target.plugins.withId("io.gitlab.arturbosch.detekt") {
            val rootProject = target.rootProject

            target.extensions.configure<DetektExtension> {
                buildUponDefaultConfig = true
                baseline = target.file("detekt-baseline.xml")
                basePath = rootProject.projectDir.absolutePath

                val localDetektConfig = target.file("detekt.yml")
                val rootDetektConfig = target.rootProject.file("detekt.yml")
                val rootDetektComposeConfig = target.rootProject.file("detekt-compose.yml")
                if (localDetektConfig.exists()) {
                    config.from(localDetektConfig, rootDetektConfig, rootDetektComposeConfig)
                } else {
                    config.from(rootDetektConfig, rootDetektComposeConfig)
                }
                source.from(
                    target.files(
                        "src/main/kotlin",
                        "src/test/kotlin",
                        "build.gradle.kts",
                        "settings.gradle.kts"
                    )
                )
            }

            val detektTask = target.tasks.named("detekt", Detekt::class.java)
            detektTask.configure {
                reports.sarif.required.set(true)
            }

            val buildDir = rootProject.layout.buildDirectory

            target.plugins.withId("collect-sarif") {
                target.tasks.named(
                    CollectSarifPlugin.MERGE_DETEKT_TASK_NAME,
                    ReportMergeTask::class.java,
                ) {
                    mustRunAfter(detektTask)
                    input.from(detektTask.map { it.sarifReportFile }.orNull)
                    output.set(
                        buildDir.file("sarifs/detekt-${target.name}.sarif").also {
                            println(it.orNull?.asFile?.absolutePath)
                        }
                    )
                }
            }
        }

        target.dependencies {
            add(
                "detektPlugins",
                target
                    .rootProject
                    .extensions
                    .getByType<VersionCatalogsExtension>()
                    .named("libs")
                    .findLibrary("detekt-compose")
                    .get()
            )
        }
    }
}
