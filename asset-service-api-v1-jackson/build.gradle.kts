plugins {
    kotlin("jvm") version "1.9.10"
    id("org.openapi.generator")
}

group = rootProject.group
version = rootProject.version

openApiGenerate {
    val openapiGroup = "${rootProject.group}.asset.api.v1"
    generatorName.set("kotlin")
    packageName.set(openapiGroup)
    apiPackage.set("$openapiGroup.api")
    modelPackage.set("$openapiGroup.models")
    invokerPackage.set("$openapiGroup.invoker")
    inputSpec.set("$rootDir/specs/asset-service-api-v1.yaml")

    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }

    configOptions.set(
        mapOf(
            "dateLibrary" to "string",
            "enumPropertyNaming" to "UPPERCASE",
            "serializationLibrary" to "jackson",
            "collectionType" to "list",
        )
    )
}

tasks {
    compileKotlin {
        dependsOn(openApiGenerate)
    }
}

sourceSets {
    main {
        java.srcDir(layout.buildDirectory.dir("generate-resources/main/src/main/kotlin"))
    }
}

dependencies {
    val jacksonVersion: String by project
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    testImplementation(kotlin("test-junit"))
}