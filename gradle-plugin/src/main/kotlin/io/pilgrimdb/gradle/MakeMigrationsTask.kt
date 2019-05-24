package io.pilgrimdb.gradle

import io.pilgrimdb.generator.exposed.ExposedStateProvider
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.TaskAction
import org.reflections.Reflections
import java.io.File
import java.io.FileNotFoundException

open class MakeMigrationsTask : DefaultTask() {

    companion object {
        const val TASK_NAME = "makemigrations"
    }

    @TaskAction
    fun generateMigrations() {
        Reflections.log = project.logger
        val packageName = "net.buluba.pilgrim.exposed"
        val results = ExposedStateProvider(packageName).getState()
        println("Hello from the Makemigrations")
        println("Results: $results")

        val kotlinPath = project.convention.getPlugin(JavaPluginConvention::class.java).sourceSets.getByName("main")
            .allSource.srcDirs.first { it.path.contains("/main/kotlin") }
            ?: throw FileNotFoundException("Kotlin folder not found")
        val packagePath = packageName.split(".").joinToString(separator = "/")
        val migrationsDir = File(kotlinPath, "$packagePath/migrations")

        if (!migrationsDir.exists()) {
            migrationsDir.mkdirs()
        }
        File(migrationsDir, "test.kt").writeText("var a = 3")
    }
}