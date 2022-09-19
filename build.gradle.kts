


plugins {
    // val springBomVersion = findProperties("spring-bom-version") as String
    // val kotlinVersion = findProperties("kotlin-version") as String
    val springBomVersion = "2.7.0"
    val dependencyManagementVersion = "1.0.11.RELEASE"
    val kotlinVersion = "1.6.21"

    id("idea")
    id("jacoco")
    id("org.springframework.boot") version springBomVersion apply false
    id("io.spring.dependency-management") version dependencyManagementVersion apply false
    id("org.flywaydb.flyway") version "6.5.7" apply false
    id("com.avast.gradle.docker-compose") version "0.15.2" apply false
    id("com.diffplug.spotless") version("6.6.1")
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion apply false
    kotlin("plugin.jpa") version kotlinVersion apply false
}
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

val ktlintVersion = "0.42.1"
val editorConfigMap = mapOf(
    "end_of_line" to "lf",
    "insert_final_newline" to "true",
    "trim_trailing_whitespace" to "true",
    "indent_style" to "space",
    "tab_width" to "4",
    "max_line_length" to "140",
    "no-unused-imports" to "true",
)

tasks {
    spotless {
        format("misc") {
            target("**/*.md", "**/*.gitignore", "**/*.xml")
            targetExclude("**/build/**", "**/.idea/**")
            trimTrailingWhitespace()
            indentWithSpaces(4)
            endWithNewline()
        }
        kotlin {
            target("**/*.kt")
            ktlint(ktlintVersion).userData(editorConfigMap)
        }
        kotlinGradle {
            target("*.gradle.kts")
            ktlint(ktlintVersion).userData(editorConfigMap)
        }
    }
}

// jacoco
// https://docs.gradle.org/current/userguide/jacoco_plugin.html
jacoco {
    toolVersion = "0.8.7"
    reportsDirectory.set(layout.buildDirectory.dir("customJacocoReportDir"))
}
tasks.test {
    // report is always generated after tests run
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
    // tests are required to run before generating the report
    dependsOn(tasks.test)
}
tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.5".toBigDecimal()
            }
        }

        rule {
            isEnabled = false
            element = "CLASS"
            // includes = listOf("org.gradle.*")

            limit {
                counter = "LINE"
                value = "TOTALCOUNT"
                maximum = "0.3".toBigDecimal()
            }
        }
    }
}
