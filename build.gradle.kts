import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("net.nemerosa.versioning") version "2.8.2" apply true
    id("org.jlleitschuh.gradle.ktlint") version "8.0.0" apply true
    id("tanvd.kosogor") version "1.0.4" apply true
    kotlin("jvm") version "1.3.31" apply true
    jacoco
}

jacoco {
    toolVersion = "0.8.2"
}

buildscript {
    repositories {
        mavenLocal()
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://kotlin.bintray.com/kotlinx")
    }

    dependencies {
        classpath("org.jlleitschuh.gradle:ktlint-gradle:8.0.0")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:0.9.18")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.3.30")
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

var ver = versioning.info.display

allprojects {
    group = "io.pilgrimdb"
    // version = versioning.info.full
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "tanvd.kosogor")

    version = rootProject.version

    repositories {
        jcenter()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs += "-Xuse-experimental=org.mylibrary.ExperimentalMarker"
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
        implementation("ch.qos.logback:logback-classic:1.2.3")

        testImplementation(kotlin("test-junit"))
        testImplementation("org.amshove.kluent:kluent:1.49")
        testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
        testImplementation("io.mockk:mockk:1.9.3")
    }
}

repositories {
    jcenter()
}
