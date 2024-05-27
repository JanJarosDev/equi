plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(libs.plugins.gitlab.detekt)
}

version = "1.0.0"

tasks.withType<Test> {
    enabled = false
}

tasks.withType<JavaCompile> {
    if (name.contains("Test")) {
        enabled = false
    }
}

tasks.named("testClasses") {
    enabled = false
}

tasks.named("compileTestJava") {
    enabled = false
}

tasks.named("processTestResources") {
    enabled = false
}

tasks.named("compileTestKotlin") {
    enabled = false
}


dependencies {
    implementation(libs.plugin.android)
    implementation(libs.plugin.detekt)
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