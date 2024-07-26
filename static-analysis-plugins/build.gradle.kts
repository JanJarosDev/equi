plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(libs.plugins.gitlab.detekt)
}

version = "1.0.0"

dependencies {
    implementation(libs.plugin.android)
    implementation(libs.plugin.detekt)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.kotlin.test.junit5)
}

gradlePlugin {
    plugins {
        create("collect-sarif") {
            id = "collect-sarif"
            implementationClass = "CollectSarifPlugin"
        }
        create("collect-sarif-lint") {
            id = "collect-sarif-lint"
            implementationClass = "LintPlugin"
        }
        create("collect-sarif-detekt") {
            id = "collect-sarif-detekt"
            implementationClass = "DetektPlugin"
        }
    }
}
