package io.pilgrimdb.common.migrations

import io.pilgrimdb.common.migrations.operations.CreateModel
import io.pilgrimdb.common.migrations.operations.Operation
import io.pilgrimdb.common.model.ProjectState

class Autodetector(private val fromState: ProjectState, private val toState: ProjectState) {

    private val operations = mutableMapOf<String, MutableList<Operation>>()

    fun changes(): Map<String, MutableList<Operation>> {
        detectChanges()
        return operations
    }

    private fun detectChanges() {
        generateCreatedModels()
    }

    private fun generateCreatedModels() {
        val addedModels = toState.models.keys - fromState.models.keys

        for ((packageName, modelName) in addedModels) {
            val newModel = toState.getModel(packageName, modelName)
            addOperation(packageName, CreateModel(newModel.name, newModel.fields))
        }
        addedModels.map { toState.models[it]!! }.forEach { newModel ->
        }
    }

    private fun addOperation(packageName: String, operation: Operation, beginning: Boolean = false) {
        when (beginning) {
            true -> operations.getOrPut(packageName, { mutableListOf() }).add(0, operation)
            false -> operations.getOrPut(packageName, { mutableListOf() }).add(operation)
        }
    }
}