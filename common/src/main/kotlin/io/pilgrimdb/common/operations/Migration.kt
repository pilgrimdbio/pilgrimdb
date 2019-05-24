package io.pilgrimdb.common.operations

import io.pilgrimdb.common.model.ProjectState

data class Migration(val operations: MutableList<Operation> = mutableListOf()) {

    fun mutateState(state: ProjectState): ProjectState {
        operations.forEach { it.stateForwards(state) }
        return state
    }
}

