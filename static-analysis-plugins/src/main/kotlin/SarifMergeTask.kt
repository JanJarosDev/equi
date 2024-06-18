import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.AbstractExecTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class SarifMergeTask : AbstractExecTask<SarifMergeTask>(SarifMergeTask::class.java) {

    @InputFiles
    @SkipWhenEmpty
    var lintSarifFiles: List<File> = emptyList()

    @OutputFile
    val mergedSarifFile: RegularFileProperty = project.objects.fileProperty()

    init {
        executable = "npx"
    }

    @TaskAction
    override fun exec() {
        val staticArguments =
            listOf(
                "@microsoft/sarif-multitool",
                "merge",
                "--force",
                "--merge-runs",
                "--recurse",
                "--output-file",
                mergedSarifFile.get().asFile.absolutePath,
            )
        val toRun = staticArguments + lintSarifFiles.map { it.absolutePath }
        println(toRun)

        args(staticArguments + lintSarifFiles.map { it.absolutePath })

        super.exec()
    }

}
