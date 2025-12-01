import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.notesmanager"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.notesmanager"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation (libs.androidx.room.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.room.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    // Compose BOM (Manages versions safely)
    implementation(platform(libs.androidx.compose.bom))
    // Core UI
    implementation(libs.androidx.compose.ui)
    implementation(platform(libs.androidx.compose.bom.v20241001))
    implementation(libs.androidx.compose.material3)
    implementation(platform(libs.androidx.compose.bom.v20240902))
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui.tooling.preview)
    // Activity
    implementation(libs.androidx.activity.compose)
    // Navigation (optional but recommended)
    implementation(libs.androidx.navigation.compose)
    // Tooling for previews
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // ViewModel + Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Runtime for StateFlow observing
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.runtime)
    kapt (libs.androidx.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}