import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    jcenter()
}

val kotlin_version: String by rootProject.extra

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlin_version}")
    implementation(project(":parsergrammar"))
}

tasks.asMap.map {
    (it.value as? KotlinCompile ?: return@map).kotlinOptions {
        jvmTarget = "1.8"
    }
}

java.setSourceCompatibility("1.8")
java.setTargetCompatibility("1.8")
