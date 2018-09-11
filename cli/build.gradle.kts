import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    id("org.jetbrains.kotlin.jvm")
}


application {
    mainClassName = "com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.cli.Main"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":core"))
    implementation(project(":mainlib"))

    implementation(project(":apksigLoader"))
}

tasks.withType(KotlinCompile::class.java) {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

java.setSourceCompatibility("1.8")
java.setTargetCompatibility("1.8")
