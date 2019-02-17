
plugins {
    setOf(
        "com.android.application",
        "kotlin-android",
        "kotlin-android-extensions"
    ).forEach {
        id(it)
    }
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "com.example.box"
        minSdkVersion(23)
        targetSdkVersion(28)
        versionCode = 1
        versionName = Release.version
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
    implementation(Packages.kotlin)
    implementation(Packages.appCompat)
    implementation(project(":samplelibrary"))
}

