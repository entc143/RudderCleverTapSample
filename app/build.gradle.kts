plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.example.rudderclevertapsample"
    compileSdk = 35
    buildToolsVersion = "35.0.0"

    defaultConfig {
        applicationId = "com.example.rudderclevertapsample"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // RudderStack Kotlin Android SDK
    implementation("com.rudderstack.sdk.kotlin:android:1.+")

    // RudderStack <> CleverTap device-mode integration
    implementation("com.rudderstack.integration.kotlin:clevertap:1.+")

    // CleverTap native Android SDK (required by the integration to call CleverTap APIs directly,
    // and pinned to the range the integration supports: [7.3.1, 7.7.0))
    implementation("com.clevertap.android:clevertap-android-sdk:7.6.0")

    // Used to build event/trait payloads for RudderStack's identify()/track() calls
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Firebase Cloud Messaging (FCM) for push token generation
    implementation(platform("com.google.firebase:firebase-bom:34.19.0"))
    implementation("com.google.firebase:firebase-messaging")
}
