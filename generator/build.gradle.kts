import tanvd.kosogor.proxy.publishJar

plugins {
    application
    id("maven") apply true
    id("kotlinx-serialization") apply true
}

repositories {
    jcenter()
    mavenCentral()
}

application {
    mainClassName = "io.pilgrimdb.generator.MainKt"
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(project(":common"))
    implementation("org.reflections:reflections:0.9.11")
    implementation("org.jetbrains.exposed:exposed:0.13.7")
    implementation("com.squareup:kotlinpoet:1.2.0")
    implementation("com.github.ajalt:clikt:2.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.11.0")
}

publishJar {
    publication {
        artifactId = "generator"
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