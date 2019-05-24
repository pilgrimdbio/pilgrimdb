package io.pilgrimdb.common

import io.pilgrimdb.common.model.ProjectState
import io.pilgrimdb.common.operations.CreateModel
import io.pilgrimdb.common.operations.Operation

class MigrationAutodetector(private val fromState: ProjectState, private val toState: ProjectState) {

    private val operations = mutableListOf<Operation>()

    fun changes(): List<Operation> {
        detectChanges()
        return operations
    }

    private fun detectChanges() {
        generateCreatedModels()
    }

    private fun generateCreatedModels() {
        val addedModels = toState.models.keys - fromState.models.keys

        addedModels.map { toState.getModel(it) }.forEach { newModel ->
            addOperation(CreateModel(newModel.name, newModel.fields))
        }
    }

    private fun addOperation(operation: Operation, beginning: Boolean = false) {
        when (beginning) {
            true -> operations.add(0, operation)
            false -> operations.add(operation)
        }
    }
}