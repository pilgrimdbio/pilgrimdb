package io.pilgrimdb.gradle.tasks

import io.pilgrimdb.common.migrations.operations.Migration
import io.pilgrimdb.generator.MigrationsRepository
import io.pilgrimdb.generator.exposed.ExposedStateProvider
import io.pilgrimdb.gradle.PilgrimExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.reflections.Reflections
import java.io.FileNotFoundException

open class MakeMigrationsTask(private val project: Project, private val pilgrimExtension: PilgrimExtension) {

    companion object {
        const val TASK_NAME = "makemigrations"
    }

    fun generateMigrations() {
        Reflections.log = project.logger

        val packageName = pilgrimExtension.scanPackage
            ?: throw IllegalArgumentException("Pilgrim `scanPackage` configuration is not set")
        project.logger.info("Using $packageName for searching models")

        val kotlinPath =
            project.convention.getPlugin(JavaPluginConvention::class.java).sourceSets.getByName("main")
                .allSource.srcDirs.first { it.path.contains("/main/kotlin") }
                ?: throw FileNotFoundException("Kotlin folder not found")

        val results = ExposedStateProvider(packageName).getState()
        println("Hello from the Makemigrations")
        println("Results: $results")

        val repo = MigrationsRepository(kotlinPath.absolutePath, packageName)
        repo.addMigration(Migration(packageName, "test1"))
    }
}