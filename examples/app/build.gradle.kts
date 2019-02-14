plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

fun Project.stuff() {
    android {
        compileSdkVersion(28)
        defaultConfig {
            applicationId = "com.example.box"
            minSdkVersion(23)
            targetSdkVersion(28)
            versionCode = 1
            versionName = "1.0"
            testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        }
        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            }
        }
    }
    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.21")
        implementation("com.android.support:appcompat-v7:28.0.0")
    }
}

stuff()