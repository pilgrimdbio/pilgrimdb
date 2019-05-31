package io.pilgrimdb.generator

import io.pilgrimdb.common.migrations.operations.Migration
import java.io.File
import java.io.FileNotFoundException
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.jvm.kotlinProperty
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.set
import mu.KotlinLogging

@Serializable
data class MigrationEntry(val packageName: String, val migrationName: String)

private val logger = KotlinLogging.logger {}

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
        addToMigrationsIndex(migration)
    }

    fun getAllMigrations(): List<Migration> {
        val indexFile = getMigrationsIndex()
        val index = if (indexFile.exists()) {
            Json.parse(MigrationEntry.serializer().set, indexFile.bufferedReader().use { it.readText() }).toMutableSet()
        } else {
            mutableSetOf()
        }

        return index.map { Class.forName("${it.packageName}.${it.migrationName}Kt") }
            .map { it.declaredFields }
            .map { it[0].kotlinProperty as KMutableProperty0<*> }
            .map { it.get() as Migration }
            .toList()
    }

    private fun getPackageDirectory(packageName: String): File {
        val packageDirectory = File(basePathFile, packageName.split(".").joinToString(separator = "/"))

        if (!packageDirectory.exists()) {
            throw FileNotFoundException("Folder ${packageDirectory.absolutePath} doesn't exists")
        }

        return packageDirectory
    }

    private fun addToMigrationsIndex(migration: Migration) {
        val indexFile = getMigrationsIndex()
        val index = if (indexFile.exists()) {
            Json.parse(MigrationEntry.serializer().set, indexFile.bufferedReader().use { it.readText() }).toMutableSet()
        } else {
            indexFile.createNewFile()
            mutableSetOf()
        }

        val indexMutable = index.toMutableSet()
        indexMutable.add(MigrationEntry(migration.packageName, migration.migrationName))
        indexFile.bufferedWriter().use { it.write(Json.stringify(MigrationEntry.serializer().set, indexMutable)) }
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

    private fun getMigrationsIndex(): File {
        // Find resources folder
        val resources = File(basePathFile.parentFile, "resources")
        // Check if it exists
        if (!resources.exists()) {
            throw FileNotFoundException("Cannot find resources directory ${resources.absolutePath}")
        }
        return File(resources, "pilgrim_index.json")
    }
}