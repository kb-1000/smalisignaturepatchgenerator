plugins {
    id("org.jetbrains.kotlin.jvm") version "1.2.51"
}


repositories {
    jcenter()
}

dependencies {
    add("implementation", "com.squareup:kotlinpoet:0.7.0")
    implementation(project("parser"))
}
