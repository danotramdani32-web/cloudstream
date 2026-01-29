import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {

    namespace = "com.lagradost.cloudstream3"

    // 🔴 WAJIB 36 (AndroidX terbaru butuh ini)
    compileSdk = 36

    defaultConfig {
        applicationId = "com.lagradost.cloudstream3"

        minSdk = 23          // Android 6 STB (AMAN)
        targetSdk = 34

        versionCode = 67
        versionName = "4.6.2"

        // 🔴 FIX tools:targetApi di AndroidManifest.xml
        manifestPlaceholders["target_sdk_version"] = "34"

        val localProperties = gradleLocalProperties(rootDir, providers)

        val simklClientId =
            System.getenv("SIMKL_CLIENT_ID")
                ?: localProperties["simkl.id"]
                ?: ""

        val simklClientSecret =
            System.getenv("SIMKL_CLIENT_SECRET")
                ?: localProperties["simkl.secret"]
                ?: ""

        // 🔴 buildConfigField WAJIB pakai string literal
        buildConfigField(
            "String",
            "SIMKL_CLIENT_ID",
            "\"$simklClientId\""
        )

        buildConfigField(
            "String",
            "SIMKL_CLIENT_SECRET",
            "\"$simklClientSecret\""
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters += "armeabi-v7a" // STB Android 6
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = false
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // 🔴 JANGAN pakai kotlin { compilerOptions } dulu
    // 🔴 JANGAN pakai tasks.withType
}

dependencies {

    coreLibraryDesugaring(libs.desugar.jdk.libs.nio)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    implementation(libs.activity.ktx)
    implementation(libs.fragment.ktx)

    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.navigation)

    implementation(libs.bundles.media3)
    implementation(libs.bundles.nextlib)

    // 🔴 WAJIB Android 6 TLS 1.2
    implementation(libs.conscrypt.android)
    implementation(libs.nicehttp)

    implementation(libs.jsoup)
    implementation(libs.jackson.module.kotlin)

    implementation(project(":library"))
}
