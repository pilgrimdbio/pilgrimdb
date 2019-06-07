import tanvd.kosogor.proxy.publishJar
import tanvd.kosogor.proxy.publishPlugin

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


publishJar {
    publication {
        artifactId = "gradle-plugin"
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

publishPlugin {
    id = "io.pilgrimdb.gradle.exposed"
    displayName = "pilgrim"
    implementationClass = "io.pilgrimdb.gradle.PilgrimPlugin"
    version = rootProject.version.toString()

    info {
        website = "https://github.com/pilgrimdbio/pilgrimdb"
        vcsUrl = "https://github.com/pilgrimdbio/pilgrimdb"
        description = "A database migration tool for kotlin, inspired from django"
        tags.addAll(listOf("migrations", "database", "kotlin"))
    }
}

dependencies {
    compileOnly(gradleApi())

    implementation(kotlin("reflect"))
    implementation(project(":common"))
    implementation(project(":generator"))
}
