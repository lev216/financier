plugins {
    kotlin("jvm") version "1.9.10"
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(project(":asset-service-common"))
    implementation(project(":asset-service-api-v1-jackson"))

    testImplementation(kotlin("test-junit"))
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.assertj:assertj-core:3.25.1")
}

