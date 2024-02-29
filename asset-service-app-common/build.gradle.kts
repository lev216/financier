plugins {
    kotlin("jvm") version "1.9.10"
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(project(":asset-service-business"))
    implementation(project(":asset-service-common"))
}
