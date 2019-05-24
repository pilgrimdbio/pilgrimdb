package io.pilgrimdb.common.operations

import io.pilgrimdb.common.MigrationBuilder
import io.pilgrimdb.common.model.Field
import io.pilgrimdb.common.model.ProjectState
import kotlin.properties.Delegates

/**
 * Base class for all field specific operations
 */
sealed class FieldOperation : Operation()

// ------------------------------------ AddField ----------------------------------
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

/**
 * Builder used in migration dsl for generating AddField operation
 *
 * @param tableName the name of the tablle to add the field
 */
class AddFieldBuilder(val tableName: String) {

    internal var field: Field by Delegates.notNull()

    fun build(): AddField {
        return AddField(tableName, field)
    }
}

/**
 * Extension method implementing the DSL for AddField
 */
fun MigrationBuilder.addField(tableName: String, setup: AddFieldBuilder.() -> Unit) {
    val addFieldBuilder = AddFieldBuilder(tableName)
    addFieldBuilder.setup()
    addOperation(addFieldBuilder.build())
}