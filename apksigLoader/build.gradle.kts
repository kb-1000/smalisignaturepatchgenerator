plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm")
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.android.tools.build:apksig:3.1.4")
}