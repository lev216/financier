rootProject.name = "financier"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openApiVersion: String by settings
        kotlin("jvm") version kotlinVersion apply false
        id("org.openapi.generator") version openApiVersion apply false
    }
}

include("asset-service")
include("currency-service")
include("cb-rf-integration-service")
