import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.HasConvention
import org.gradle.api.tasks.SourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm")
}

val smali_version: String by rootProject.extra

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("org.smali:baksmali:${smali_version}") {
        exclude(group = "com.google.guava")
    }

    // The following one block contains updates to dependencies of other dependencies.
    //noinspection GradleVersion
    implementation("com.beust:jcommander:1.71")
    implementation("com.google.guava:guava:26.0-jre")

    implementation(project(":parser"))
}

val patchDefGeneratedDirectory = File(projectDir, "src/main/java")
val patchDefSourceDirectory = File(projectDir, "src/main/patchdef")


java.setSourceCompatibility("1.8")
java.setTargetCompatibility("1.8")



val SourceSet.kotlin: SourceDirectorySet
    get() =
        (this as HasConvention)
                .convention
                .getPlugin(KotlinSourceSet::class.java)
                .kotlin


fun SourceSet.kotlin(action: SourceDirectorySet.() -> Unit) =
        kotlin.action()

java.sourceSets {
    getByName("main").java.srcDirs += patchDefGeneratedDirectory
    getByName("main").kotlin.srcDirs += patchDefGeneratedDirectory
}

val compilePatchDef = tasks.create("compilePatchDef") {
    doFirst {
        PatchDefCompiler.generate(patchDefSourceDirectory, patchDefGeneratedDirectory)
    }
}

tasks.getByName<KotlinCompile>("compileKotlin") {
    dependsOn(compilePatchDef)
}
