plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

val datetimeVersion: String by project
dependencies {
    api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
}