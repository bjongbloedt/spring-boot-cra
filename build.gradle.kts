import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("com.github.node-gradle.node")
}

group = "com.bjongbloedt"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

val testContainersVersion: String by extra
val nodeVersion: String by extra

repositories {
    mavenCentral()
}

node {
    version = nodeVersion
    download = true
    workDir = file(".gradle/nodejs")
    npmWorkDir = file(".gradle/npm")
    nodeModulesDir = file("${project.projectDir}/src/main/javascript")
}

tasks {
    register("npmBuild", com.moowork.gradle.node.npm.NpmTask::class) {
        dependsOn("npmSetup")
        dependsOn("npmInstall")
        dependsOn("npm_test")
        setArgs(mutableListOf("run", "build"))
    }

    register("copyReactToBuild", Copy::class) {
        dependsOn("npmBuild")
        from("$projectDir/src/main/javascript/build/")
        into("$buildDir/resources/main/static")
    }

    bootJar {
        dependsOn("copyReactToBuild")
    }

    bootBuildImage {
        imageName = "docker.io/benjongbloedt/widget-service:${project.version}"
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:$testContainersVersion")
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
