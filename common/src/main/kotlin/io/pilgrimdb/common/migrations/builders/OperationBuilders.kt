package io.pilgrimdb.common.migrations.builders

import io.pilgrimdb.common.migrations.operations.AddField
import io.pilgrimdb.common.migrations.operations.CreateModel
import io.pilgrimdb.common.migrations.operations.Operation
import io.pilgrimdb.common.model.Field
import kotlin.properties.Delegates

interface OperationBuilderDSL {

    fun addOperation(operation: Operation)
}

// ----------------------------- CreateModel -------------------------------
/**
 * Builder used in migration dsl for generating CreateModel operation
 * @see CreateModel
 *
 * @param name the name of the table to add the field
 */
class CreateModelBuilder(val name: String) : AllFieldsBuilderDSL {

    private val fields = mutableListOf<Field>()

    override fun addField(field: Field) {
        this.fields.add(field)
    }

    fun build(): CreateModel {
        return CreateModel(name, fields)
    }
}

/**
 * Interface for creating [CreateModel] operation using DSL
 *
 * Example usage:
 * ```
 * createModel("tableName") {
 *     autoField("fieldName")
 * }
 * ```
 */
interface CreateModelBuilderDSL : OperationBuilderDSL {

    fun createModel(name: String, setup: CreateModelBuilder.() -> Unit) {
        val createModelBuilder = CreateModelBuilder(name)
        createModelBuilder.setup()
        addOperation(createModelBuilder.build())
    }
}

// ----------------------------- AddField -------------------------------
/**
 * Builder used in migration dsl for generating AddField operation
 *
 * @param tableName the name of the tablle to add the field
 */
class AddFieldBuilder(val tableName: String) : AllFieldsBuilderDSL {

    internal var field: Field by Delegates.notNull()

    override fun addField(field: Field) {
        this.field = field
    }

    fun build(): AddField {
        return AddField(tableName, field)
    }
}

/**
 * Interface for creating [AddField] operation using DSL
 *
 * Example usage:
 * ```
 * addField("tableName") {
 *     autoField("fieldName")
 * }
 * ```
 */
interface AddFieldBuilderDSL : OperationBuilderDSL {

    fun addField(name: String, setup: AddFieldBuilder.() -> Unit) {
        val addFieldBuilder = AddFieldBuilder(name)
        addFieldBuilder.setup()
        addOperation(addFieldBuilder.build())
    }
}

/**
 * Barrel interface for all operation builders
 */
interface AllOperationBuildersDSL :
    CreateModelBuilderDSL,
    AddFieldBuilderDSL