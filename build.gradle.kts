import com.google.cloud.tools.gradle.appengine.standard.AppEngineStandardExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("com.google.cloud.tools:appengine-gradle-plugin:1.3.5")
    }
}

plugins {
    val kotlinVersion = "1.3.30"

    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    java
    war
}

apply {
    plugin("com.google.cloud.tools.appengine")
}

repositories {
    jcenter()
}

dependencies {
    implementation("javax.servlet:javax.servlet-api:3.1.0")
    implementation("com.google.appengine:appengine:+")
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.squareup.moshi:moshi:1.8.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.8.0")

    implementation("com.google.actions:actions-on-google:1.0.0")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("com.google.firebase:firebase-admin:6.7.0")

    testImplementation("junit:junit:4.12")
}

// Since we apply the app engine after the plugins {} block, we cannot rely on type-safe accessors
// See https://github.com/GoogleCloudPlatform/app-gradle-plugin/issues/191
the<AppEngineStandardExtension>().deploy {
    stopPreviousVersion = true
    promote = true
}

version = "1.0"

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}