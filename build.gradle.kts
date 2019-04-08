import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    val kotlinVersion: String by project // from gradle.properties

    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath("com.google.cloud.tools:appengine-gradle-plugin:1.+")
        classpath("org.akhikhl.gretty:gretty:+")
        classpath(kotlin("gradle-plugin", kotlinVersion))
    }
}

plugins {
    kotlin("jvm") // version is set on settings.gradle file
    java
    war
}

apply {
    plugin("com.google.cloud.tools.appengine")
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("javax.servlet:javax.servlet-api:3.1.0")
    implementation("com.google.appengine:appengine:+")
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.google.actions:actions-on-google:1.0.0")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("com.google.firebase:firebase-admin:6.7.0")
    
    testImplementation("junit:junit:4.12")
}

appengine {
    deploy {
        stopPreviousVersion = true
        promote = true
    }
} // https://github.com/GoogleCloudPlatform/app-gradle-plugin/issues/191#issuecomment-367443306

version = "1.0"

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}