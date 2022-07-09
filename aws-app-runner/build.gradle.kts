import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    //id("com.google.cloud.tools.jib") version "3.1.4"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
    id("com.avast.gradle.docker-compose") version "0.15.2"
}

val boostrapVersion = "5.1.3"
val fontAwesomeVersion = "4.7.0"

group = "org.springframework.samples"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("javax.cache:cache-api")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.webjars:webjars-locator-core")
    implementation("org.webjars.npm:bootstrap:$boostrapVersion")
    implementation("org.webjars.npm:font-awesome:$fontAwesomeVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

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
}

dockerCompose {
    useComposeFiles.add("./docker-compose.override.yml")
    isRequiredBy(project.tasks.getByName("bootRun"), )
}

//jib {
//    to {
//        image = "springcommunity/spring-petclinic-kotlin"
//        tags = setOf(project.version.toString(), "latest")
//    }
//}
