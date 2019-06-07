import org.gradle.internal.impldep.org.bouncycastle.crypto.tls.BulkCipherAlgorithm.idea
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import tanvd.kosogor.proxy.publishJar

plugins {
    kotlin("jvm") apply true
    maven
    idea
    id("kotlinx-serialization") apply true
}

repositories {
    jcenter()
    mavenCentral()
}

sourceSets {
    create("intTest") {
        withConvention(KotlinSourceSet::class) {
            kotlin.srcDir("src/intTest/kotlin")
            resources.srcDir("src/intTest/resources")
            compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
            runtimeClasspath += output + compileClasspath + sourceSets["test"].runtimeClasspath
        }
    }
}

task<Test>("integrationTest") {
    description = "Runs the integration tests"
    group = "verification"
    testClassesDirs = sourceSets["intTest"].output.classesDirs
    classpath = sourceSets["intTest"].runtimeClasspath
    systemProperty("io.pilgrim.common.test.postgres.enabled", project.property("io.pilgrim.common.test.postgres.enabled").toString())
    systemProperty("io.pilgrim.common.test.postgres.url", project.property("io.pilgrim.common.test.postgres.url").toString())
    systemProperty("io.pilgrim.common.test.postgres.user", project.property("io.pilgrim.common.test.postgres.user").toString())
    systemProperty("io.pilgrim.common.test.postgres.password", project.property("io.pilgrim.common.test.postgres.password").toString())

    val excludeTasks = mutableSetOf<String>()
    if (project.property("io.pilgrim.common.test.postgres.enabled").toString() == "false") {
        excludeTasks += "postgres"
    }
    useJUnitPlatform {
        excludeTags = excludeTasks
    }
    testLogging {
        events("passed", "skipped", "failed")
    }
    mustRunAfter(tasks["test"])
}

idea.module {
    val testSources = testSourceDirs

    project.sourceSets.getByName("intTest").withConvention(KotlinSourceSet::class) {
        testSources.addAll(kotlin.srcDirs)
        testSources.addAll(resources.srcDirs)
    }
    testSourceDirs = testSources
}

val intTestImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

configurations["intTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

dependencies {
    intTestImplementation("org.postgresql:postgresql:42.2.5")
    intTestImplementation("ch.qos.logback:logback-classic:1.2.3")
    intTestImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    intTestImplementation("com.h2database:h2:1.4.197")
}

dependencies {
    api(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.11.0")
    implementation(kotlin("reflect"))
    implementation("com.github.seratch:kotliquery:1.3.0")
    implementation("org.apache.commons", "commons-text", "1.6")
    testImplementation(kotlin("test-junit"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testImplementation("com.h2database:h2:1.4.197")
}

publishJar {
    publication {
        artifactId = "common"
    }

    bintray {
        username = project.properties["bintrayUser"]?.toString() ?: System.getenv("BINTRAY_USER")
        secretKey = project.properties["bintrayApiKey"]?.toString() ?: System.getenv("BINTRAY_API_KEY")
        repository = "pilgrimdb"
        info {
            githubRepo = "pilgrimdbio/pilgrimdb"
            vcsUrl = "https://github.com/pilgrimdbio/pilgrimdb"
            userOrg = "pilgrimdbio"
            license = "Apache-2.0"
        }
    }
}