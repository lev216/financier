plugins {
    kotlin("jvm")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.otus.otuskotlin.financier.currency.MainKt"
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
}