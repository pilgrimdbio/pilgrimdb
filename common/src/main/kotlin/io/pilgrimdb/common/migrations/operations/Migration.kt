package io.pilgrimdb.common.migrations.operations

import io.pilgrimdb.common.migrations.ProjectState

data class Migration(val operations: MutableList<Operation> = mutableListOf()) {

    fun mutateState(state: ProjectState): ProjectState {
        operations.forEach { it.stateForwards(state) }
        return state
    }
}
