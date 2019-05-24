import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    kotlin("jvm") version "1.3.31" apply true
    id("org.jlleitschuh.gradle.ktlint") version "8.0.0" apply true
    jacoco
}

jacoco {
    toolVersion = "0.8.2"
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

tasks.register<JacocoReport>("codeCoverageReport") {
    executionData(fileTree(project.rootDir.absolutePath).include("**/build/jacoco/*.exec"))

    subprojects.onEach {
        println(it)
        sourceSets(it.project.sourceSets["main"])
    }

    reports {
        xml.isEnabled = true
        xml.destination = File("$buildDir/reports/jacoco/report.xml")
        html.isEnabled = false
        csv.isEnabled = false
    }

    dependsOn("test")
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")

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