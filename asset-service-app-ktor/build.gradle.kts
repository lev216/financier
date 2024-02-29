plugins {
    kotlin("jvm") version "1.9.10"
    id("application")
    id("io.ktor.plugin")
}

val ktorVersion: String by project
val logbackVersion: String by project

fun ktor(module: String, version: String? = ktorVersion): Any =
    "io.ktor:ktor-$module:$version"
fun ktorServer(module: String, version: String? = ktorVersion): Any =
    "io.ktor:ktor-server-$module:$version"
fun ktorClient(module: String, version: String? = ktorVersion): Any =
    "io.ktor:ktor-client-$module:$version"

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

ktor {
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(JavaVersion.VERSION_17)
    }
}

jib {
    container.mainClass = "io.ktor.server.cio.EngineMain"
}

dependencies {
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    implementation(kotlin("stdlib-common"))
    implementation(ktorServer("core"))
    implementation(ktorServer("cio"))
    implementation(ktorServer("auto-head-response"))
    implementation(ktorServer("caching-headers"))
    implementation(ktorServer("cors"))
    implementation(ktorServer("config-yaml"))
    implementation(ktorServer("content-negotiation"))

    implementation(ktor("serialization-jackson"))
    implementation(ktorServer("call-logging"))
    implementation(ktorServer("default-headers"))

    implementation(project(":asset-service-api-v1-jackson"))
    implementation(project(":asset-service-api-v1"))
    implementation(project(":asset-service-common"))
    implementation(project(":asset-service-app-common"))
    implementation(project(":asset-service-business"))

    testImplementation(kotlin("test-junit"))
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.assertj:assertj-core:3.25.1")
    testImplementation(ktorServer("test-host"))
    testImplementation(ktorClient("content-negotiation"))
    testImplementation("org.assertj:assertj-core:3.25.1")
}