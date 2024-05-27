plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(libs.plugins.gitlab.detekt)
}

version = "1.0.0"

dependencies {
    implementation(libs.plugin.android)
    implementation(libs.plugin.detekt)
}

gradlePlugin {
    plugins {
        create("appyx-collect-sarif") {
            id = "appyx-collect-sarif"
            implementationClass = "CollectSarifPlugin"
        }
        create("appyx-lint") {
            id = "appyx-lint"
            implementationClass = "LintPlugin"
        }
        create("appyx-detekt") {
            id = "appyx-detekt"
            implementationClass = "DetektPlugin"
        }
    }
}