import org.gradle.api.tasks.JavaExec
import java.net.HttpURLConnection
import java.net.URL

plugins {
    id("java")
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.flywaydb.flyway") version "11.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // DB access and migration
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    // File Export
    implementation("org.mnode.ical4j:ical4j:4.0.8")
    implementation("org.apache.poi:poi-ooxml:5.3.0")

    // Others
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.0")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    // Test
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("generateOpenApiYaml") {
    group = "openapi"
    description = "Generates the OpenAPI YAML file."

    doLast {
        val url = URL("http://localhost:8080/v3/api-docs.yaml")
        var connection: HttpURLConnection? = null
        var isAvailable = false
        var retries = 0
        val maxRetries = 10
        val retryDelay = 2000L // 2 seconds

        while (!isAvailable && retries < maxRetries) {
            try {
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000 // 5 seconds timeout
                connection.readTimeout = 5000 // 5 seconds timeout

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    isAvailable = true
                }
            } catch (e: Exception) {
                // Wait before retrying
                Thread.sleep(retryDelay)
            } finally {
                connection?.disconnect()
            }
            retries++
        }

        if (!isAvailable) {
            println("Failed to connect to the Spring Boot application after $maxRetries attempts.")
            return@doLast
        }

        // Fetch the OpenAPI YAML file
        try {
            val yamlContent = url.readText()

            // Save to a file
            val outputFile = file("$projectDir/api/openapi.yaml")
            outputFile.parentFile.mkdirs()
            outputFile.writeText(yamlContent)

            println("OpenAPI YAML file generated successfully at: ${outputFile.absolutePath}")
        } catch (e: Exception) {
            println("Failed to generate OpenAPI YAML file: ${e.message}")
        }
    }
}
