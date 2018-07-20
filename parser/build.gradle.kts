plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm")
    antlr
}

repositories {
    jcenter()
}

dependencies {
    antlr("org.antlr:antlr4:4.7.1")
}
