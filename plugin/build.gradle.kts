@file:Suppress("UnstableApiUsage")

plugins {
    java
    `kotlin-dsl`
    `maven-publish`
    `java-gradle-plugin`
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka") version "1.9.10"
}

group = "dev.kikugie"
version = "0.4.0-alpha.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":stitcher"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("script-runtime"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(16)
}

publishing {
    repositories {
        maven {
            name = "kikugieMaven"
            url = uri("https://maven.kikugie.dev/snapshots")
            credentials(PasswordCredentials::class)
            authentication {
                create("basic", BasicAuthentication::class)
            }
        }
    }

    publications {
        register("mavenJava", MavenPublication::class) {
            groupId = project.group.toString()
            artifactId = "stonecutter"
            version = project.version.toString()
            artifact(tasks.getByName("jar"))
        }
    }
}

gradlePlugin {
    website = "https://github.com/kikugie/stonecutter-kt"
    vcsUrl = "https://github.com/kikugie/stonecutter-kt"

    plugins {
        create("stonecutter") {
            id = "dev.kikugie.stonecutter"
            implementationClass = "dev.kikugie.stonecutter.gradle.StonecutterPlugin"
            displayName = "Stonecutter"
            description = "Preprocessor/JCP inspired multi-version environment manager"
            tags.set(listOf("fabric", "fabricmc"))
        }
    }
}