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
}
