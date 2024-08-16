plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlinx.kover)
    alias(libs.plugins.hilt)
  //  alias(libs.plugins.screenshot)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.jjdev.equi"
    compileSdk = COMPILE_SDK_VERSION

    defaultConfig {
        applicationId = "com.jjdev.equi"
        minSdk = MIN_SDK_VERSION
        targetSdk = TARGET_SDK_VERSION
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

    kapt {
        correctErrorTypes = true
    }

    hilt {
        enableAggregatingTask = true
    }

/*
    Ready for screenshot testing
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
*/

    tasks.withType(Test::class) {
        jvmArgs("-Dnet.bytebuddy.experimental=true", "-XX:+EnableDynamicAgentLoading")
    }
}

dependencies {
    implementation(project(":core"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.navigation)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.compose)
    kapt(libs.hilt.ext.compiler)
    implementation(libs.kotlinx.collections)

    //Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.activity.compose)

    //Logging
    implementation(libs.timber)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.coroutines.test)
    debugImplementation(libs.androidx.compose.ui.tooling)

    //Testing
  //  screenshotTestImplementation(libs.androidx.compose.ui.tooling)
    testImplementation(libs.mockk)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.turbine)
}
