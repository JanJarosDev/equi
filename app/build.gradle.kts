
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlinx.kover)
}

android {
    namespace = "com.jjdev.equi"
    compileSdk = COMPILE_SDK_VERSION

    defaultConfig {
        applicationId = "com.jjdev.equi"
        minSdk = MIN_SDK_VERSION
        versionCode = VersioningUtils.generateVersionCode()
        versionName = VersioningUtils.getVersionFromTag(project)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = JVM_TARGET_VERSION
    }
}

dependencies {
    implementation(project(":core"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.material3.android)

    //Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.tooling.preview)
    implementation(libs.compose.runtime)
    implementation(libs.compose.activity)

    //Logging
    implementation(libs.timber)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.junit.jupiter.api)
    debugImplementation(libs.androidx.ui.tooling)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.kotlin.test.junit5)
}
