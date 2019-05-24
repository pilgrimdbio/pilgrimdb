package io.pilgrimdb.common

import io.pilgrimdb.common.model.ProjectState
import io.pilgrimdb.common.operations.Operation

data class Migration(val operations: MutableList<Operation> = mutableListOf()) {

    fun mutateState(state: ProjectState): ProjectState {
        operations.forEach { it.stateForwards(state) }
        return state
    }
}

class MigrationBuilder {

    private val operations = mutableListOf<Operation>()

    fun build(): Migration {
        return Migration(operations)
    }

    internal fun addOperation(operation: Operation) {
        operations += operation
    }
}

fun migration(setup: MigrationBuilder.() -> Unit): Migration {
    val migrationBuilder = MigrationBuilder()
    migrationBuilder.setup()
    return migrationBuilder.build()
}

