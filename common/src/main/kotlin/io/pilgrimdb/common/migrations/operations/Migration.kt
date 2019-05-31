package io.pilgrimdb.common.migrations.operations

import io.pilgrimdb.common.model.ProjectState

data class Migration(
    val packageName: String,
    val migrationName: String,
    val operations: MutableList<Operation> = mutableListOf()
) {

    fun mutateState(state: ProjectState): ProjectState {
        operations.forEach { it.stateForwards(packageName, state) }
        return state
    }
}
