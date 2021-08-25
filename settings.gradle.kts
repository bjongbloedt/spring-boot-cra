rootProject.name = "widget"

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val gradleNodeVersion: String by settings
    plugins {

        kotlin("jvm") version kotlinVersion
        kotlin("jvm") version kotlinVersion

        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion

        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.jpa") version kotlinVersion
        id("com.github.node-gradle.node") version gradleNodeVersion
    }
}