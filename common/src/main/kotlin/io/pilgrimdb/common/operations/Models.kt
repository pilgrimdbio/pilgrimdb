package io.pilgrimdb.common.operations

import io.pilgrimdb.common.model.Field
import io.pilgrimdb.common.model.ModelState
import io.pilgrimdb.common.model.ProjectState

/**
 * Base class for model operations
 */
sealed class ModelOperation : Operation()

// ------------------------------------ CreateModel --------------------------------------

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
