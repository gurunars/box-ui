plugins {
    setOf(
        "kotlin",
        "java-library"
    ).forEach {
        id(it)
    }
}

group = "com.gurunars.binding"
version = Release.version

dependencies {
    testImplementation(Packages.junit)
    implementation(Packages.rx)
    implementation(Packages.kotlin)
}