plugins {
    kotlin("jvm") apply true
    id("maven") apply true
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation(project(":generator"))
    implementation(kotlin("reflect"))

    implementation("org.jetbrains.exposed:exposed:0.13.7")
    implementation("org.reflections:reflections:0.9.11")
}
