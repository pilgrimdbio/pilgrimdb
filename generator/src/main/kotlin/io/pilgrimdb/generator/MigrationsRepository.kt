package io.pilgrimdb.generator

import io.pilgrimdb.common.migrations.operations.Migration
import java.io.File
import java.io.FileNotFoundException
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.set
import mu.KotlinLogging

@Serializable
data class MigrationEntry(val migrationName: String)

private val logger = KotlinLogging.logger {}

@UnstableDefault
class MigrationsRepository(basePath: String) {

    private val basePathFile: File = File(basePath)

    init {
        if (!basePathFile.exists()) {
            throw FileNotFoundException("Folder ${basePathFile.absolutePath} doesn't exists")
        }
    }

    fun addMigration(migration: Migration) {
        logger.debug { "Adding migration: ${migration.migrationName} in package ${migration.packageName}" }
        val migrationsDirectory = getOrCreateMigrationsDirectory(migration.packageName)
        val content = MigrationRenderer(migration).render()
        File(migrationsDirectory, "${migration.migrationName}.kt").writeText(content)
        // addToMigrationsIndex(migration)
    }

    fun getAllMigrations(packageName: String): List<String> {
        val migrationsFolder = File(getPackageDirectory(packageName), "migrations")

        if (!migrationsFolder.exists()) {
            // No migration folder exists, return empty
            return listOf()
        }
        return migrationsFolder.listFiles().filter { !it.isDirectory && it.extension == "kt" }.map { it.name }.toList()
    }

    private fun getOrCreateMigrationsDirectory(packageName: String): File {
        val dir = File(getPackageDirectory(packageName), "migrations")

        if (dir.exists()) {
            return dir
        }
        logger.debug { "Creating migrations folder for $packageName" }
        if (!dir.mkdir()) {
            throw FileNotFoundException("Cannot create migration directory ${dir.absolutePath}")
        }

        return dir
    }

    private fun getPackageDirectory(packageName: String): File {
        val packageDirectory = File(basePathFile, packageName.split(".").joinToString(separator = "/"))

        if (!packageDirectory.exists()) {
            throw FileNotFoundException("Folder ${packageDirectory.absolutePath} doesn't exists")
        }

        return packageDirectory
    }

    private fun addToMigrationsIndex(migration: Migration) {
        val indexFile = File(basePathFile, "../resources")
        val index =
            Json.parse(MigrationEntry.serializer().set, indexFile.bufferedReader().use { it.readText() }).toMutableSet()

        index.toMutableSet().add(MigrationEntry(("${migration.packageName}.${migration.migrationName}")))

        indexFile.bufferedWriter().use { it.write(Json.stringify(MigrationEntry.serializer().set, index)) }
    }
}