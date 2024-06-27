import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.dokka) apply true
    alias(libs.plugins.gitlab.detekt) apply true
    alias(libs.plugins.collect.sarif) apply false
    alias(libs.plugins.jetbrains.kotlinx.kover) apply true
    alias(libs.plugins.compose.compiler) apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jetbrains.kotlin.plugin.compose")

    tasks.withType<DokkaTaskPartial>().configureEach {
        outputDirectory.set(layout.buildDirectory.dir("docs/partial"))
    }

    plugins.withId("io.gitlab.arturbosch.detekt") {
        extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
            buildUponDefaultConfig = true
            allRules = true
            ignoreFailures = true
        }

        tasks.withType<Detekt>().configureEach {
            reports {
                sarif.required.set(true)
            }
            jvmTarget = "1.8"
        }

        tasks.withType<DetektCreateBaselineTask>().configureEach {
            jvmTarget = "1.8"
        }
    }

    if (project.name != "static-analysis-plugins") {
        apply(plugin = "collect-sarif")
        apply(plugin = "collect-sarif-detekt")
        apply(plugin = "collect-sarif-lint")
    }
}

dependencies {
    kover(project(":app"))
    kover(project(":core"))
}

rootProject.tasks.register("mergeSarif", ReportMergeTask::class.java) {
    group = JavaBasePlugin.VERIFICATION_GROUP
    output.set(rootProject.layout.buildDirectory.file("final.sarif"))
    input = rootProject.layout.buildDirectory.dir("sarifs/").get().asFileTree
}

kover {
    reports {
        verify {
            rule {
                minBound(100)
            }
        }
        filters {
            excludes {
                classes("com.jjdev.equi.ui.theme.*")
            }
        }
    }
}