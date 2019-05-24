package io.pilgrimdb.common.migrations

import io.pilgrimdb.common.migrations.operations.CreateModel
import io.pilgrimdb.common.migrations.operations.Operation
import io.pilgrimdb.common.model.ProjectState

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