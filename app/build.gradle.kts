import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

val javaTarget = JvmTarget.fromTarget(libs.versions.jvmTarget.get())

android {

    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.lagradost.cloudstream3"
        minSdk = 23            // AMAN untuk Android 6
        targetSdk = 34

        versionCode = 67
        versionName = "4.6.2"

        val localProperties = gradleLocalProperties(rootDir, providers)

        buildConfigField(
            "String",
            "SIMKL_CLIENT_ID",
            (System.getenv("SIMKL_CLIENT_ID")
                ?: localProperties["simkl.id"]
                ?: ""
            ).toString()
        )

        buildConfigField(
            "String",
            "SIMKL_CLIENT_SECRET",
            (System.getenv("SIMKL_CLIENT_SECRET")
                ?: localProperties["simkl.secret"]
                ?: ""
            ).toString()
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters += listOf("armeabi-v7a") // WAJIB untuk STB Android 6
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

    kotlinOptions {
        jvmTarget = "17"
    }

    namespace = "com.lagradost.cloudstream3"
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

    implementation(libs.conscrypt.android) // PENTING Android 6 TLS
    implementation(libs.nicehttp)

    implementation(libs.jsoup)
    implementation(libs.jackson.module.kotlin)

    implementation(project(":library"))
}

tasks.withType<KotlinJvmCompile> {
    compilerOptions {
        jvmTarget.set(javaTarget)
        jvmDefault.set(JvmDefaultMode.ENABLE)
        freeCompilerArgs.add("-Xannotation-default-target=param-property")
    }
}
