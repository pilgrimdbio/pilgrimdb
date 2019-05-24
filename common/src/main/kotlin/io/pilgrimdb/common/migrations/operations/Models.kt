package io.pilgrimdb.common.migrations.operations

import io.pilgrimdb.common.migrations.ModelState
import io.pilgrimdb.common.migrations.ProjectState
import io.pilgrimdb.common.model.Field

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
