plugins {
  kotlin("jvm")
}

val logbackVersion: String by project

dependencies {
  implementation("ch.qos.logback:logback-classic:$logbackVersion")
  implementation(project(":asset-service-common"))
  implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.0.1")

  testImplementation("org.testcontainers:mongodb:1.19.7")
  testImplementation(project(":asset-service-persistence-tests"))
}