package io.pilgrimdb.common.migrations

import io.pilgrimdb.common.db.base.Connection
import io.pilgrimdb.common.model.AutoField
import io.pilgrimdb.common.model.CharField
import io.pilgrimdb.common.model.ModelState
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class MigrationRecorder(val connection: Connection) {

    private val migrationModel = ModelState("pilgrim_migrations", mutableListOf(
        AutoField("id", primaryKey = true, index = true, unique = true, nullable = false),
        CharField("package", primaryKey = false, index = false, unique = false, nullable = false, maxLength = 150),
        CharField("name", primaryKey = false, index = false, unique = false, nullable = false, maxLength = 150)
    ))

    private fun hasTable(): Boolean {
        return connection.introspection.getTableNames().firstOrNull { it.name == migrationModel.name } != null
    }

    fun ensureSchema() {
        // If table already exists do nothing
        if (hasTable()) {
            logger.debug { "Migrations table already exists" }
            return
        }
        logger.info { "Creating migrations table" }
        connection.schemaEditor.createModel(migrationModel)
    }

}