plugins {
    kotlin("jvm")
}

dependencies {
    val kafkaVersion: String by project
    api("org.apache.kafka:kafka-clients:$kafkaVersion")

    testImplementation("org.assertj:assertj-core:3.25.1")
    testImplementation(kotlin("test-junit"))
    testImplementation("io.mockk:mockk:1.13.8")
}