import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    kotlin("jvm") apply true
    maven
    idea
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
        excludeTasks +=  "postgres"
    }
    useJUnitPlatform {
        excludeTags = excludeTasks
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
    implementation("com.github.seratch:kotliquery:1.3.0")
    implementation("org.apache.commons", "commons-text", "1.6")
    testApi(kotlin("reflect"))
    testImplementation(kotlin("test-junit"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testImplementation("com.h2database:h2:1.4.197")
}
