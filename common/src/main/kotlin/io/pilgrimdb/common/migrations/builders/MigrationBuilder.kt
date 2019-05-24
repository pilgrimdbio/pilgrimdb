package io.pilgrimdb.common.migrations.builders

import io.pilgrimdb.common.migrations.operations.Migration
import io.pilgrimdb.common.migrations.operations.Operation

class MigrationBuilder : AllOperationBuildersDSL {

    private val operations = mutableListOf<Operation>()

    fun build(): Migration {
        return Migration(operations)
    }

    override fun addOperation(operation: Operation) {
        operations += operation
    }
}

fun migration(setup: MigrationBuilder.() -> Unit): Migration {
    val migrationBuilder = MigrationBuilder()
    migrationBuilder.setup()
    return migrationBuilder.build()
}
