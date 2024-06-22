import org.gradle.api.Project
import java.io.ByteArrayOutputStream

object VersioningUtils {
    fun generateVersionCode(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }

    fun getVersionFromTag(project: Project): String {
        return try {
            val stdout = ByteArrayOutputStream()
            project.exec {
                commandLine = "git describe --tags --abbrev=0".split(" ")
                standardOutput = stdout
            }
            stdout.toString().trim()
        } catch (e: Exception) {
            "0.0.0"
        }
    }
}