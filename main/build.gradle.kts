plugins {
    application
    id("org.jetbrains.kotlin.jvm")
}

val kotlin_version: String by rootProject.extra

tasks.withType(CreateStartScripts::class.java).getByName("startScripts") {
    //classpath = files("lib/SmaliSignaturePatchGenerator.jar") //TODO: include this after adding ProGuard
}

application {
    mainClassName = "com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.Main"
    applicationName = "smali-signature-patch-generator"

}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation(project(":mainlib"))


    implementation(project(":cli"))
}
