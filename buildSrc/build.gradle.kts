apply(from = "../dependencies.gradle.kts")

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.2.61"
}


repositories {
    jcenter()
}

val kotlinPoetVersion: String by rootProject.extra

dependencies {
    add("implementation", "com.squareup:kotlinpoet:$kotlinPoetVersion")
    implementation(project("parser"))
}
