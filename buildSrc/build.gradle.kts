buildscript {
    repositories {
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21")
    }
}

repositories {
    jcenter()
    google()
    mavenCentral()
}


plugins {
    `kotlin-dsl`
}

dependencies {
    val kotlinVersion = "1.3.21"
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
}