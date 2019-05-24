package io.pilgrimdb.common.migrations.operations

import io.pilgrimdb.common.model.Field
import io.pilgrimdb.common.model.ProjectState

/**
 * Base class for all field specific operations
 */
sealed class FieldOperation : Operation()

// ------------------------------------ AddField ------------------------------------
/**
 * Adds a new field in a table
 * @param tableName the name of the table
 * @param field the field instance describing the field
 */
data class AddField(val tableName: String, val field: Field) : FieldOperation() {

    override fun stateForwards(state: ProjectState) {
        state.getModel(tableName).fields += field
    }
}
