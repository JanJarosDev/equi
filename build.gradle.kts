import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.dokka) apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.6" apply false
    id("appyx-collect-sarif")
}

subprojects {

    if (name != "collect-sarif-plugin") {
        apply(plugin = "org.jetbrains.dokka")

        plugins.withId("org.jetbrains.dokka") {
            tasks.register<Jar>("dokkaJar") {
                from(tasks.named("dokkaHtml"))
                dependsOn(tasks.named("dokkaHtml"))
                archiveClassifier.set("javadoc")
            }
        }
    }

    apply(plugin = "io.gitlab.arturbosch.detekt")

    plugins.withId("io.gitlab.arturbosch.detekt") {
        extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
            buildUponDefaultConfig = true
            allRules = true
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
}
