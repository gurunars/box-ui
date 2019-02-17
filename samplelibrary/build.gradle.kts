plugins {
    setOf(
        "com.android.library",
        "kotlin-android",
        "kotlin-android-extensions"
    ).forEach {
        id(it)
    }
}

android {
    compileSdkVersion(28)
    defaultConfig {
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
}
repositories {
    mavenCentral()
}