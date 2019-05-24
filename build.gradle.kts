import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    kotlin("jvm") version "1.3.31" apply true
    id("org.jlleitschuh.gradle.ktlint") version "8.0.0" apply true
}

buildscript {
    repositories {
        mavenLocal()
        maven(
            url = "https://plugins.gradle.org/m2/"
        )
    }

    dependencies {
        classpath("org.jlleitschuh.gradle:ktlint-gradle:8.0.0")
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    ktlint {
        outputToConsole.set(true)
        reporters.set(setOf(ReporterType.PLAIN, ReporterType.CHECKSTYLE))
        ignoreFailures.set(true)
        enableExperimentalRules.set(true)
    }

    dependencies {
        api("org.slf4j", "slf4j-api", "1.7.25")
        implementation(kotlin("stdlib"))

        testImplementation(kotlin("test-junit"))
        testImplementation("org.amshove.kluent:kluent:1.49")
        testImplementation("org.slf4j", "slf4j-log4j12", "1.7.26")
        testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
        testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.16")
    }
}

repositories {
    jcenter()
}