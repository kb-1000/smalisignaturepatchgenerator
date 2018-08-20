plugins {
    application
    id("org.jetbrains.kotlin.jvm")
}

val kotlin_version: String by rootProject.extra

val kotlinPoetVersion: String by rootProject.extra

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    implementation("com.squareup:kotlinpoet:$kotlinPoetVersion")
    implementation(project(":parser"))
    implementation(project(":parsergrammar"))
}

application {
    mainClassName = "com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.updateParser.Main"
}

java.setSourceCompatibility("1.8")
java.setTargetCompatibility("1.8")
