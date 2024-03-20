plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(project(":asset-service-business"))
    implementation(project(":asset-service-common"))
}
