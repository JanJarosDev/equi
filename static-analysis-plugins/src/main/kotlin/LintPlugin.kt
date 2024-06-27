import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.lint.AndroidLintTask
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class LintPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.plugins.withId("com.android.library") {
            collectLintSarif(target)
        }
        target.plugins.withId("com.android.application") {
            collectLintSarif(target)
        }
    }

    private fun collectLintSarif(target: Project) {
        val buildDir = target.rootProject.layout.buildDirectory

        target.plugins.withId("collect-sarif") {
            target.tasks.named(
                CollectSarifPlugin.MERGE_LINT_TASK_NAME,
                ReportMergeTask::class.java,
            ) {
                input.from(
                    target
                        .tasks
                        .named("lintReportDebug", AndroidLintTask::class.java)
                        .flatMap { it.sarifReportOutputFile }
                )
                output.set(buildDir.file("sarifs/lint-${target.name}.sarif").also {
                    println(it.orNull?.asFile?.absolutePath)
                })
            }
        }
    }

}
