import com.github.gradle.node.npm.task.NpmTask

plugins {
    base
    id("com.github.node-gradle.node")
}

val nodeVersion: String by extra
val jsFolder = "${project.projectDir}/src/main/javascript"

node {
    version.set(nodeVersion)
    download.set(true)
    workDir.set(file(".gradle/nodejs"))
    npmWorkDir.set(file(".gradle/npm"))
    nodeProjectDir.set(file(jsFolder))
}

tasks {
    named<NpmTask>("npm_run_build") {
        inputs.files(fileTree("$jsFolder/public"))
        inputs.files(fileTree("$jsFolder/src"))

        inputs.file("$jsFolder/package.json")
        inputs.file("$jsFolder/package-lock.json")

        outputs.dir("build")
    }

    val packageWebApp = register<Jar>("webJar") {
        dependsOn("npm_run_build")
        archiveBaseName.set("webapp")
        archiveExtension.set("jar")
        destinationDirectory.set(file("${projectDir}/build"))
        from("$jsFolder/build") {
            into("static")
        }

    }

    artifacts {
        add("default", packageWebApp.get().archiveFile) {
            builtBy(packageWebApp)
            type = "jar"
        }
    }

    assemble {
        dependsOn(packageWebApp)
    }

    clean {
        delete(packageWebApp.get().archiveFile)
        delete(file("$jsFolder/build"))
    }

    check {
        dependsOn("npm_run_test")
    }
}