plugins {
    kotlin("jvm") apply true
    maven
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    api(kotlin("stdlib"))
    api("org.slf4j", "slf4j-api", "1.7.25")

    testApi(kotlin("reflect"))
    testImplementation(kotlin("test-junit"))
    testImplementation("org.slf4j", "slf4j-log4j12", "1.7.26")
    testImplementation("log4j", "log4j", "1.2.17")
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.16")
}

tasks.withType(Test::class.java) {
    jvmArgs = listOf("-XX:MaxPermSize=256m")
    testLogging {
        showStandardStreams = true
    }
}
