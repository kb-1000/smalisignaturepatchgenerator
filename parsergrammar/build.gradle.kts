plugins {
    `java-library`
    antlr
}

repositories {
    jcenter()
}

val antlr_version: String by rootProject.extra

dependencies {
    antlr("org.antlr:antlr4:${antlr_version}")
    api("org.antlr:antlr4-runtime:${antlr_version}")
}

java.setSourceCompatibility("1.8")
java.setTargetCompatibility("1.8")
