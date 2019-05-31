package io.pilgrimdb.common.migrations

import io.pilgrimdb.common.db.base.Connection
import io.pilgrimdb.common.model.AutoField
import io.pilgrimdb.common.model.CharField
import io.pilgrimdb.common.model.ModelState
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class Recorder(val connection: Connection) {

    private val recorderModelName = "pilgrim_migrations"

    private val migrationModel = ModelState(
        recorderModelName,
        mutableListOf(
            AutoField("id", primaryKey = true, index = true, unique = true, nullable = false),
            CharField("package", primaryKey = false, index = false, unique = false, nullable = false, maxLength = 150),
            CharField("name", primaryKey = false, index = false, unique = false, nullable = false, maxLength = 150)
        )
    )

    fun ensureSchema() {
        // If table already exists do nothing
        if (hasTable()) {
            logger.debug { "Migrations table already exists" }
            return
        }
        logger.info { "Creating migrations table" }
        connection.schemaEditor.createModel(migrationModel)
    }

    fun getAppliedMigrations() =
        connection.query("SELECT package, name FROM $recorderModelName") {
            Pair(it.string(1), it.string(2))
        }.toMap()

    fun recordApplied(packageName: String, name: String) {
        // Todo: Possible sql injection
        connection.execute("INSERT INTO $recorderModelName (package, name) VALUES ('$packageName', '$name')")
    }

    fun recordUnapplied(packageName: String, name: String) {
        // Todo: Possible sql injection
        connection.execute("DELETE FROM $recorderModelName WHERE package='$packageName' AND name='$name'")
    }

    private fun hasTable(): Boolean {
        return connection.introspection.getTableNames().firstOrNull { it.name == migrationModel.name } != null
    }
}