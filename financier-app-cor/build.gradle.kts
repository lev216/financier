plugins {
    kotlin("jvm")
}

dependencies {
    val coroutinesVersion: String by project
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    testImplementation(kotlin("test-junit"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    testImplementation("org.assertj:assertj-core:3.25.1")
}