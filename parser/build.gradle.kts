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

java.setSourceCompatibility("1.8")
java.setTargetCompatibility("1.8")
