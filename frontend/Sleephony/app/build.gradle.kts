import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

val localProps = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use(::load)
}

val kakaoNativeKey: String =
    localProps["KAKAO_NATIVE_APP_KEY"] as? String ?: error("KAKAO_NATIVE_APP_KEY가 없습니다.")

val googleOauthClientId: String =
    localProps["GOOGLE_OAUTH_CLIENT_ID"] as? String ?:error("GOOGLE_OAUTH_CLIENT_ID가 없습니다.")

val sleephonyBaseUrl: String =
    localProps["SLEEPHONY_BASE_URL"] as? String ?:error("SLEEPHONY_BASE_URL이 없습니다.")


android {
    namespace = "com.example.sleephony"
    compileSdk = 35

    buildFeatures {
        compose = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.sleephony"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoNativeKey\"")
        buildConfigField("String", "GOOGLE_OAUTH_CLIENT_ID", "\"$googleOauthClientId\"")
        buildConfigField("String", "SLEEPHONY_BASE_URL", "\"$sleephonyBaseUrl\"")

        resValue("string", "kakao_redirect_url", "kakao${kakaoNativeKey}")

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    //material
    implementation("androidx.compose.material:material:1.6.1")

    // 달력
    implementation("com.kizitonwose.calendar:compose:2.6.0")
    implementation("com.kizitonwose.calendar:view:2.6.0")

    //chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")


    // 달력
    implementation("com.kizitonwose.calendar:compose:2.6.0")
    implementation("com.kizitonwose.calendar:view:2.6.0")

    //chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

        // wear 통신
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation("com.google.android.gms:play-services-wearable:18.1.0")


    // Hilt
    implementation("com.google.dagger:hilt-android:2.56.1")
    implementation(libs.places)
    ksp("com.google.dagger:hilt-android-compiler:2.56.1")
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
    implementation("androidx.core:core-ktx:1.16.0")

    // DataStore
    implementation(libs.androidx.datastore.preferences)
    implementation("androidx.datastore:datastore-preferences-core:1.1.6")

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

    // Kakao Login
    val LATEST_VERSION = "2.21.2"
    implementation("com.kakao.sdk:v2-all:${LATEST_VERSION}") // 전체 모듈 설치, 2.11.0 버전부터 지원
    implementation("com.kakao.sdk:v2-user:${LATEST_VERSION}") // 카카오 로그인 API 모듈
    implementation("com.kakao.sdk:v2-share:${LATEST_VERSION}") // 카카오톡 공유 API 모듈
    implementation("com.kakao.sdk:v2-talk:${LATEST_VERSION}") // 카카오톡 채널, 카카오톡 소셜, 카카오톡 메시지 API 모듈
    implementation("com.kakao.sdk:v2-friend:${LATEST_VERSION}") // 피커 API 모듈
    implementation("com.kakao.sdk:v2-navi:${LATEST_VERSION}") // 카카오내비 API 모듈
    implementation("com.kakao.sdk:v2-cert:${LATEST_VERSION}") // 카카오톡 인증 서비스 API 모듈

    // google 로그인
    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")

    // utils
    implementation("dev.chrisbanes.snapper:snapper:0.3.0")
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation ("com.github.commandiron:WheelPickerCompose:1.1.11")

    // sse
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:okhttp-sse:4.12.0")

    // Test
    testImplementation            (libs.junit)
    androidTestImplementation     (libs.androidx.junit)
    androidTestImplementation     (libs.androidx.espresso.core)
    androidTestImplementation     (platform(libs.androidx.compose.bom))
    androidTestImplementation     (libs.androidx.ui.test.junit4)
    debugImplementation           (libs.androidx.ui.test.manifest)
}
