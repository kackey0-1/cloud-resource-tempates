import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    // val springBomVersion = findProperties("spring-bom-version") as String
    // val kotlinVersion = findProperties("kotlin-version") as String
    val springBomVersion = "2.7.0"
    val dependencyManagementVersion = "1.0.11.RELEASE"
    val kotlinVersion = "1.6.21"

    id("idea")
    id("org.springframework.boot") version springBomVersion
    id("io.spring.dependency-management") version dependencyManagementVersion
    id("com.diffplug.spotless") version("6.6.1")
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
}

group = "org.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.1")
    }
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Development
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // Functional Test
    functionalTestImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    functionalTestImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    functionalTestImplementation("org.springframework.boot:spring-boot-starter-test")
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
    bootRun {
        jvmArgs = ArrayList(jvmArgs).apply {
            add("-Dspring.profiles.active=local")
        }
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

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
