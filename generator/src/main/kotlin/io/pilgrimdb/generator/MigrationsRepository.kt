package io.pilgrimdb.generator

import io.pilgrimdb.common.migrations.operations.Migration
import java.io.File
import java.io.FileNotFoundException

class MigrationsRepository(basePath: String, val packageName: String) {

    private val packageDirectory: File

    init {
        val basePath = File(basePath)
        if (!basePath.exists()) {
            throw FileNotFoundException("Folder ${basePath.absolutePath} doesn't exists")
        }

        packageDirectory = File(basePath, packageName.split(".").joinToString(separator = "/"))

        if (!packageDirectory.exists()) {
            throw FileNotFoundException("Folder ${packageDirectory.absolutePath} doesn't exists")
        }
    }

    fun addMigration(migration: Migration) {
        val migrationsDirectory = getOrCreateMigrationsDirectory()
        val content = MigrationRenderer(migration).render()
        File(migrationsDirectory, "${migration.migrationName}.kt").writeText(content)
    }

    fun getMigrationsNames(): List<String> {
        val migrationsFolder = File(packageDirectory, "migrations")

        if (!migrationsFolder.exists()) {
            // No migration folder exists, return empty
            return listOf()
        }

        return migrationsFolder.listFiles().filter { !it.isDirectory && it.extension == "kt" }.map { it.name }.toList()
    }

    private fun getOrCreateMigrationsDirectory(): File {
        val dir = File(packageDirectory, "migrations")

        if (dir.exists()) {
            return dir
        }

        if (!dir.mkdir()) {
            throw FileNotFoundException("Cannot create migration directory ${dir.absolutePath}")
        }

        return dir
    }

    private fun migrationFolderExists() {
    }
}