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
    implementation("com.github.seratch:kotliquery:1.3.0")
    implementation("org.apache.commons", "commons-text", "1.6")
    testApi(kotlin("reflect"))
    testImplementation(kotlin("test-junit"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testImplementation("com.h2database:h2:1.4.197")
}
