plugins {
    kotlin("jvm") version "1.9.10"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.otus.otuskotlin.financier.asset.MainKt"
    }
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {
    implementation("ch.qos.logback:logback-classic:1.2.10")
    testImplementation(kotlin("test-junit"))
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.assertj:assertj-core:3.25.1")
}