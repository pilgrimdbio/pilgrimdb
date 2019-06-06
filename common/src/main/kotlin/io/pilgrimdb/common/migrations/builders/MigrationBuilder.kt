package io.pilgrimdb.common.migrations.builders

import io.pilgrimdb.common.migrations.operations.Migration
import io.pilgrimdb.common.migrations.operations.MigrationIndex
import io.pilgrimdb.common.migrations.operations.Operation

class MigrationBuilder(val packageName: String, val migrationName: String) : AllOperationBuildersDSL {

    private val operations = mutableListOf<Operation>()
    private val dependencies = mutableListOf<MigrationIndex>()

    fun build(): Migration {
        return Migration(packageName, migrationName, dependencies, operations = operations)
    }

    override fun addOperation(operation: Operation) {
        operations += operation
    }

    fun dependsOn(packageName: String, migrationName: String) {
        dependencies += MigrationIndex(packageName, migrationName)
    }
}

fun migration(packageName: String, migrationName: String, setup: MigrationBuilder.() -> Unit = {}): Migration {
    val migrationBuilder = MigrationBuilder(packageName, migrationName)
    migrationBuilder.setup()
    return migrationBuilder.build()
}
