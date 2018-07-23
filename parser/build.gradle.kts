plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm")
    antlr
}

repositories {
    jcenter()
}

val kotlin_version: String by rootProject.extra

dependencies {
    antlr("org.antlr:antlr4:4.7.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlin_version}")
}
