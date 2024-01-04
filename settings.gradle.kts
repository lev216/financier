rootProject.name = "financier"

pluginManagement {
    val kotlinVersion: String by settings
    val openApiVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion apply false
        id("org.openapi.generator") version openApiVersion apply false
    }
}

include("asset-service")
include("currency-service")
include("cb-rf-integration-service")
include("asset-service-api-v1")
include("asset-service-common")
include("asset-service-api-v1-jackson")
