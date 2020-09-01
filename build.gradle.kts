import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
    kotlin("plugin.jpa") version "1.3.72"
    id("com.github.node-gradle.node") version "2.2.4"
}

group = "com.bjongbloedt"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

node {
    version = "10.20.1"
    download = true
    workDir = file(".gradle/nodejs")
    npmWorkDir = file(".gradle/npm")
    nodeModulesDir = file("${project.projectDir}/src/main/javascript")
}

tasks {
    register("npmBuild", com.moowork.gradle.node.npm.NpmTask::class) {
        dependsOn("npmSetup")
        dependsOn("npmInstall")
        setArgs(mutableListOf("run", "build"))
    }

    register("copyReactToBuild", Copy::class) {
        dependsOn("npmBuild")
        from("$projectDir/src/main/javascript/build/")
        into("$buildDir/resources/main/static")
    }

    processResources {
        dependsOn("copyReactToBuild")
    }
}

extra["testcontainersVersion"] = "1.14.3"

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

//    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
