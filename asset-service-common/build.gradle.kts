plugins {
    kotlin("jvm") version "1.9.10"
}

group = rootProject.group
version = rootProject.version

val datetimeVersion: String by project
dependencies {
    api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
}