plugins {
    application
    id("maven") apply true
    id("kotlinx-serialization") apply true
}

repositories {
    jcenter()
    mavenCentral()
}

application {
    mainClassName = "io.pilgrimdb.generator.MainKt"
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(project(":common"))
    implementation(project(":generator:exposed"))
    implementation("com.squareup:kotlinpoet:1.2.0")
    implementation("com.github.ajalt:clikt:2.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.11.0")
}
