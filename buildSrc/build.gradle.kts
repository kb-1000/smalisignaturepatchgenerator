apply(from = "../dependencies.gradle.kts")

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.2.60"
}


repositories {
    jcenter()
}

val kotlin_version: String by rootProject.extra
val kotlinPoetVersion: String by rootProject.extra

dependencies {
    add("implementation", "com.squareup:kotlinpoet:$kotlinPoetVersion")
    implementation(project("parser"))
}
