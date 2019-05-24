import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.io.File

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
        jcenter()
        maven(
            url = "https://plugins.gradle.org/m2/"
        )
    }

    dependencies {
        classpath("org.jlleitschuh.gradle:ktlint-gradle:8.0.0")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:0.9.18")
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
    apply(plugin = "org.jetbrains.dokka")

    repositories {
        jcenter()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.test {
        useJUnitPlatform()

        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    ktlint {
        outputToConsole.set(true)
        reporters.set(setOf(ReporterType.PLAIN, ReporterType.CHECKSTYLE))
        ignoreFailures.set(true)
        enableExperimentalRules.set(true)
    }

    dependencies {
        implementation(kotlin("stdlib"))
        implementation("io.github.microutils:kotlin-logging:1.6.24")

        testImplementation(kotlin("test-junit"))
        testImplementation("org.amshove.kluent:kluent:1.49")
        testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
        testImplementation("io.mockk:mockk:1.9.3")
    }
}

repositories {
    jcenter()
}