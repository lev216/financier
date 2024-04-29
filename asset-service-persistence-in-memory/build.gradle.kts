plugins {
    kotlin("jvm")
}

dependencies {
    val cache4kVersion: String by project
    val coroutinesVersion: String by project

    implementation(project(":asset-service-common"))
    implementation("io.github.reactivecircus.cache4k:cache4k:$cache4kVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    testImplementation(project(":asset-service-persistence-tests"))
}
