plugins {
    `java-gradle-plugin`
    kotlin("jvm") apply true
    kotlin("kapt") apply true
    id("maven-publish")
    id("maven") apply true
}

repositories {
    jcenter()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("Gradle pilgrim plugin") {
            id = "io.pilgrimdb.gradle.exposed"
            implementationClass = "io.pilgrimdb.gradle.PilgrimPlugin"
        }
    }
}

publishing {
    // used for publishing to local maven repository
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.pilgrimdb.gradle"
            artifactId = "exposed"
            version = "0.0.1"

            from(components["kotlin"])
        }
    }
}

dependencies {
    compileOnly(gradleApi())

    implementation(kotlin("reflect"))
    implementation(project(":common"))
    implementation(project(":generator"))
    implementation(project(":generator:exposed"))

    implementation("org.jetbrains.exposed:exposed:0.13.7")
    implementation("org.reflections:reflections:0.9.11")
}
