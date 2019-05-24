package io.pilgrimdb.common.operations

import io.pilgrimdb.common.MigrationBuilder
import io.pilgrimdb.common.model.Field
import io.pilgrimdb.common.model.ModelState
import io.pilgrimdb.common.model.ProjectState

/**
 * Base class for model operations
 */
sealed class ModelOperation : Operation()

// ------------------------------------ CreateModel ----------------------------------

/**
 * Operation for creating a new table
 * @param name the new table name
 * @param fields the fields of the new table
 */
data class CreateModel(val name: String, val fields: MutableList<Field> = mutableListOf()) : ModelOperation() {

    override fun stateForwards(state: ProjectState) {
        state.addModel(ModelState(name, fields))
    }
}

class CreateModelBuilder(val name: String) {

    internal val fields = mutableListOf<Field>()

    fun build(): CreateModel {
        return CreateModel(name, fields)
    }
}

fun MigrationBuilder.createModel(name: String, setup: CreateModelBuilder.() -> Unit) {
    val createModelBuilder = CreateModelBuilder(name)
    createModelBuilder.setup()
    addOperation(createModelBuilder.build())
}