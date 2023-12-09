rootProject.name = "financier"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion apply false
    }
}

include("asset-service")
include("currency-service")
include("cb-rf-integration-service")
