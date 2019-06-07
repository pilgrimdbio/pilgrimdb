package io.pilgrimdb.generator

import io.pilgrimdb.common.migrations.Autodetector
import io.pilgrimdb.common.migrations.Repository
import io.pilgrimdb.common.migrations.operations.Migration
import io.pilgrimdb.generator.state.StateProvider
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class MigrationGenerator(private val basePath: String, private val stateProvider: StateProvider) {

    fun makeMigrations() {
        logger.debug("Starting action: makemigrations")
        val repository = Repository(basePath)
        val graph = repository.getGraph()

        val currentState = graph.makeState()
        val targetState = stateProvider.getState()

        val autodetector = Autodetector(currentState, currentState.mergeState(targetState))
        val changes = autodetector.changes()

        logger.info("Found changes: $changes")
        for ((packageName, operations) in changes) {
            val migration = Migration(packageName, generateName(), operations = operations)
            val migrationsRepository = Repository(basePath)
            migrationsRepository.addMigration(migration, MigrationRenderer(migration).render())
        }
    }

    private fun generateName(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val time = LocalDateTime.now().format(formatter)
        return "Migration$time"
    }
}