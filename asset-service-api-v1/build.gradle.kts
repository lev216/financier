plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val logbackVersion: String by project
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation(project(":asset-service-common"))
    implementation(project(":asset-service-api-v1-jackson"))

    testImplementation(kotlin("test-junit"))
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.assertj:assertj-core:3.25.1")
}

