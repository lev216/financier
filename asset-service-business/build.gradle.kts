plugins {
    kotlin("jvm")
}

val coroutinesVersion: String by project

dependencies {
    implementation(project(":asset-service-common"))
    implementation(project(":asset-service-stubs"))
    implementation(project(":financier-app-cor"))

    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(kotlin("test-junit"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    testImplementation("org.assertj:assertj-core:3.25.1")
}