import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    kotlin("plugin.serialization") version "2.2.20"
    alias(libs.plugins.detekt)
    alias(libs.plugins.google.services)
    id("de.jensklingenberg.ktorfit") version "2.7.1"
}

detekt {
    buildUponDefaultConfig = true
    toolVersion = libs.versions.detekt.get()
    config.setFrom(files("$rootDir/detekt-config.yml"))
}

android {
    namespace = "com.konkuk.medicarecall"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.konkuk.medicarecall"
        minSdk = 26
        targetSdk = 36
        versionCode = 10
        versionName = "0.1.9"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties().apply {
            load(project.rootProject.file("local.properties").inputStream())
        }

        buildConfigField("String", "BASE_URL", "\"${properties["base.url"] ?: ""}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
        buildConfig = true
    }
}

dependencies {
    // AndroidX & Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.play.services.vision.common)

    // DataStore (명시적으로 추가)
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    // WebView
    implementation("com.google.accompanist:accompanist-webview:0.24.13-rc")

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.analytics)
    implementation(libs.firebase.messaging)

    // Detekt formatting plugin
    detektPlugins(libs.detekt.formatting)

    // Ktor
    implementation("io.ktor:ktor-client-auth:3.3.3")
    implementation("io.ktor:ktor-client-logging:3.3.3")
    implementation("io.ktor:ktor-client-content-negotiation:3.3.3") // Use latest version
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.3")

    // Ktorfit
    implementation("de.jensklingenberg.ktorfit:ktorfit-lib:2.7.1")
    implementation("de.jensklingenberg.ktorfit:ktorfit-converters-response:2.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = "11"
}

ksp {
    arg("KOIN_DEFAULT_MODULE", "true")
}
