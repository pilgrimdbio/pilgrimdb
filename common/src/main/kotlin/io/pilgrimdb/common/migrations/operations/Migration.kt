package io.pilgrimdb.common.migrations.operations

import io.pilgrimdb.common.model.ProjectState
import kotlinx.serialization.Serializable

@Serializable
data class MigrationIndex(val packageName: String, val migrationName: String)

data class Migration(
    val packageName: String,
    val migrationName: String,
    val dependencies: MutableList<MigrationIndex> = mutableListOf(),
    val operations: MutableList<Operation> = mutableListOf()
) {

    val index = MigrationIndex(packageName, migrationName)

    fun mutateState(state: ProjectState): ProjectState {
        operations.forEach { it.stateForwards(packageName, state) }
        return state
    }
}
