rootProject.name = "financier"

pluginManagement {
    val kotlinVersion: String by settings
    val openApiVersion: String by settings
    val ktorVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("org.openapi.generator") version openApiVersion apply false
        id("io.ktor.plugin") version ktorVersion apply false
    }
}

include("asset-service-app-ktor")
include("currency-service")
include("cb-rf-integration-service")
include("asset-service-api-v1")
include("asset-service-common")
include("asset-service-api-v1-jackson")
include("asset-service-business")
include("asset-service-stubs")
include("asset-service-app-common")
include("financier-app-kafka")
include("client-socket")
