plugins {
    setOf(
        "kotlin",
        "java-library"
    ).forEach {
        id(it)
    }
}

group = "com.gurunars.binding"
version = "1.0"

dependencies {
    testImplementation("junit:junit:4.12")
    implementation("io.reactivex.rxjava2:rxjava:2.1.13")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.21")
}