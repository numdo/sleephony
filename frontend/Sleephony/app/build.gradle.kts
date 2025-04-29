plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.sleephony"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.sleephony"
        minSdk = 29
        targetSdk = 35
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
        compose = true
    }
}

dependencies {
    // Hilt
    implementation(libs.hilt.android)
    kapt         (libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Retrofit & Gson & OkHttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp.logging)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp           (libs.androidx.room.compiler)

    // Accompanist
    implementation(libs.accompanist.systemuicontroller)

    // Lifecycle & Activity
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)

    // Test
    testImplementation            (libs.junit)
    androidTestImplementation     (libs.androidx.junit)
    androidTestImplementation     (libs.androidx.espresso.core)
    androidTestImplementation     (platform(libs.androidx.compose.bom))
    androidTestImplementation     (libs.androidx.ui.test.junit4)
    debugImplementation           (libs.androidx.ui.test.manifest)
}
