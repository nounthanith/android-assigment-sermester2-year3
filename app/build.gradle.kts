plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.salu.project"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.salu.project"
        minSdk = 24
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
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
//Gson converter library
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
// Kotlin Coroutines Core
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.11.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
// ViewModel utilities for Coroutine scope (highly recommended)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
}