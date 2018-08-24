plugins {
    application
    id("org.jetbrains.kotlin.jvm")
}

val kotlin_version: String by rootProject.extra

application {
    mainClassName = "com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.Main"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation(project(":mainlib"))

    
    implementation(project(":cli"))
}
