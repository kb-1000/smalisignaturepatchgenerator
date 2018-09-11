import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}


tasks.withType(KotlinCompile::class.java) {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

java.setSourceCompatibility("1.8")
java.setTargetCompatibility("1.8")
