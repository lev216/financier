plugins {
    kotlin("jvm")
}

dependencies {
    val coroutinesVersion: String by project
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    api(kotlin("test-common"))
    api(kotlin("test-annotations-common"))
    api(kotlin("test-junit"))
    api("org.assertj:assertj-core:3.25.1")

    implementation(project(":asset-service-common"))
}