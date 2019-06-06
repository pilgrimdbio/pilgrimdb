package io.pilgrimdb.generator

import io.pilgrimdb.common.migrations.Autodetector
import io.pilgrimdb.common.migrations.Repository
import io.pilgrimdb.common.migrations.operations.Migration
import io.pilgrimdb.common.migrations.providers.StateProvider
import io.pilgrimdb.common.model.ProjectState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class MigrationGenerator(private val basePath: String, private val stateProvider: StateProvider) {

    fun makeMigrations() {
        logger.debug("Starting action: makemigrations")

        val targetState = stateProvider.getState()

        val autodetector = Autodetector(ProjectState(), targetState)
        val changes = autodetector.changes()

        logger.info("Found chnages: $changes")
        for ((packageName, operations) in changes) {
            val migration = Migration(packageName, generateName(), operations = operations)
            val migrationsRepository = Repository(basePath)
            migrationsRepository.addMigration(migration, MigrationRenderer(migration).render())
        }
    }

    private fun generateName(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        return LocalDateTime.now().format(formatter)
    }
}