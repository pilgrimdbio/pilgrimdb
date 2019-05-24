plugins {
    id("maven") apply true
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(project(":common"))
    implementation("com.squareup:kotlinpoet:1.2.0")
}
