import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm")
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.android.tools.build:apksig:3.1.4")
}

tasks.withType(KotlinCompile::class.java) {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

java.setSourceCompatibility("1.8")
java.setTargetCompatibility("1.8")
