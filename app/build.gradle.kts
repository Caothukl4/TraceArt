import java.util.Date
import java.text.SimpleDateFormat

plugins {
    id("com.google.devtools.ksp")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "2.2.20"
}

android {
    namespace = "com.aktech.ardrawing"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.aktech.ardrawing"
        minSdk = 24
        targetSdk = 36
        versionCode = 3
        versionName = "1.3"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "env"

    productFlavors {
        create("dev") {
            dimension = "env"
            versionCode = 1
            versionName = "1.0"
            resValue("string", "app_name", "VoicyBook")
            buildConfigField("String", "BASE_URL", "\"https://api-test.example.com\"")
        }
        create("product") {
            dimension = "env"
            versionCode = 28
            versionName = "1.5.2"
            resValue("string", "app_name", "VoicyBook")
            buildConfigField("String", "BASE_URL", "\"https://api.example.com\"")
        }
    }

    bundle {
        language {
            // Ngăn Google Play tách ngôn ngữ -> giữ nguyên trong base APK
            enableSplit = false
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            applicationVariants.all {
                outputs.all {
                    val date = Date()
                    val formattedDate = SimpleDateFormat("yyyyMMdd.HHmmss").format(date)

                    val apkName = "VoicyBook_${flavorName}_${versionName}_${formattedDate}.apk"

                    // Kotlin DSL: phải cast sang com.android.build.gradle.internal.api.BaseVariantOutputImpl
                    (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName = apkName
                }
            }
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
    implementation(libs.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.exifinterface)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)

    // Navigation Compose
    implementation(libs.navigation.compose)

    // Datasource
    implementation(libs.androidx.datastore)

    // constraintlayout
    implementation(libs.androidx.constraintlayout)

    // material
    implementation(libs.android.material)

    // Hilt (DI)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Firebase (BoM + modules bạn dùng)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.functions)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.remoteconfig)
    implementation(libs.firebase.appcheck)
    implementation(libs.firebase.appcheck.playintegrity)
    implementation(libs.firebase.appcheck.debug)

    // Google service
    implementation(libs.playservices.auth)
    implementation(libs.playservices.ads)

    // CameraX
    implementation(libs.camerax.core)
    implementation(libs.camerax.camera2)
    implementation(libs.camerax.lifecycle)
    implementation(libs.camerax.view)

    // uCrop (image crop)
    implementation(libs.ucrop)

    // Media3 (phát âm thanh + notification)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.session)
    implementation(libs.media3.ui)
    implementation(libs.media3.datasource)
    implementation(libs.media3.datasource.okhttp)

    // API call: Retrofit + OkHttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    // MLKit
    implementation(libs.mlkit.text.recognition)
    implementation(libs.mlkit.text.recognition.chinese)
    implementation(libs.mlkit.text.recognition.devanagari)
    implementation(libs.mlkit.text.recognition.japanese)
    implementation(libs.mlkit.text.recognition.korean)

    // PageCurl
    implementation(libs.pagecurl)

    // Play Review
    implementation(libs.play.review)
    implementation(libs.play.review.ktx)

    //jtokkit
    implementation(libs.jtokkit)
    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}