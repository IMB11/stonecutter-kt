@file:Suppress("UnstableApiUsage")

import org.jetbrains.dokka.gradle.DokkaTask


plugins {
    java
    `kotlin-dsl`
    `maven-publish`
    `java-gradle-plugin`
    kotlin("jvm")
    kotlin("plugin.serialization")
}

val stonecutter: String by project

group = "dev.kikugie"
version = stonecutter

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":stitcher"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.1")
    implementation("com.charleskorn.kaml:kaml:0.57.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<DokkaTask>().configureEach {
    moduleName.set("Stonecutter Plugin")
    dokkaSourceSets {
        configureEach {
            reportUndocumented = true
            skipEmptyPackages = true
        }
    }
}

kotlin {
    jvmToolchain(16)
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.named<Jar>("javadocJar") {
    from(tasks.named("dokkaJavadoc"))
}

publishing {
    repositories {
        maven {
            name = "kikugieMaven"
            url = uri("https://maven.kikugie.dev/releases")
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
            from(components["java"])
        }
    }
}

gradlePlugin {
    website = "https://github.com/kikugie/stonecutter-kt"
    vcsUrl = "https://github.com/kikugie/stonecutter-kt"

    plugins {
        create("stonecutter") {
            id = "dev.kikugie.stonecutter"
            implementationClass = "dev.kikugie.stonecutter.StonecutterPlugin"
            displayName = "Stonecutter"
            description = "Preprocessor/JCP inspired multi-version environment manager"
            tags.set(listOf("fabric", "fabricmc"))
        }
    }
}