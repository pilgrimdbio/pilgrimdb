plugins {
    application
    kotlin("jvm") version "1.3.31" apply true
    id("io.pilgrimdb.gradle.exposed") version "0.0.1" apply true
}

buildscript {
    repositories {
        mavenLocal()
        maven(
            url = "https://plugins.gradle.org/m2/"
        )
    }
}

application {
    mainClassName = "io.pilgrimdb.sample.exposed.MainKt"
}

pilgrim {
    scanPackage = "io.pilgrimdb.sample.exposed"
    dbUrl = "jdbc:postgresql://localhost:5432/pilgrimdb"
    dbUser = "pilgrimdb"
    dbPassword = "pilgrimdb"
}

repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
}

dependencies {
    api(kotlin("stdlib"))
    api("org.slf4j", "slf4j-api", "1.7.25")
    implementation("org.jetbrains.exposed:exposed:0.13.7")
    implementation("io.pilgrimdb:common:0.0.1")
    testImplementation(kotlin("test-junit"))
    testImplementation("org.slf4j", "slf4j-log4j12", "1.7.26")
    testImplementation("log4j", "log4j", "1.2.17")
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.16")
}
