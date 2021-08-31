import com.github.gradle.node.npm.task.NpmTask
import org.jetbrains.kotlin.gradle.targets.js.npm.fromSrcPackageJson
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("com.github.node-gradle.node")
}

java.sourceCompatibility = JavaVersion.VERSION_11

val testContainersVersion: String by extra
val nodeVersion: String by extra

repositories {
    mavenCentral()
}

node {
    version.set(nodeVersion)
    download.set(true)
    workDir.set(file(".gradle/nodejs"))
    npmWorkDir.set(file(".gradle/npm"))
    nodeProjectDir.set(file("${project.projectDir}/src/main/javascript"))
    fromSrcPackageJson(file("${project.projectDir}/src/main/javascript/package.json"))
}

tasks {


    register<NpmTask>("npmBuild") {
        dependsOn("npmSetup")
        dependsOn("npmInstall")
        dependsOn("npm_test")
        args.set(mutableListOf("run", "build"))
    }

    register("copyReactToBuild", Copy::class) {
        dependsOn("npmBuild")
        from("$projectDir/main/javascript/build/")
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
