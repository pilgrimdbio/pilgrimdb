package io.pilgrimdb.common.migrations.builders

import io.pilgrimdb.common.model.AutoField
import io.pilgrimdb.common.model.CharField
import io.pilgrimdb.common.model.Field
import io.pilgrimdb.common.model.IntegerField
import kotlin.properties.Delegates

/**
 * Base class for field builders
 */
sealed class FieldBuilder {

    /**
     * name of the field
     */
    var name: String by Delegates.notNull()

    /**
     * if the field is primary key
     */
    var primaryKey = false

    /**
     * if the field should be indexed
     */
    var index = false

    /**
     * if the field is unique
     */
    var unique = false

    /**
     * if the field is nullable
     */
    var nullable = false

    /**
     * returns a Field instance
     * @see Field
     */
    abstract fun build(name: String): Field
}

interface FieldBuilderDSL {

    fun addField(field: Field)
}

// --------------------------------- AutoField ---------------------------------

/**
 * Builder for [AutoField]
 * @see AutoField
 */
class AutoFieldBuilder : FieldBuilder() {

    override fun build(name: String): AutoField {
        return AutoField(name, primaryKey, index, unique, nullable)
    }
}

/**
 * Interface for creating [AutoField] using DSL
 *
 * Example usage:
 * ```
 * autoField("fieldName") {
 *     primaryKey = true
 * }
 * ```
 */
interface AutoFieldBuilderDSL : FieldBuilderDSL {

    fun autoField(name: String, setup: AutoFieldBuilder.() -> Unit = {}) {
        val fieldBuilder = AutoFieldBuilder()
        fieldBuilder.setup()
        addField(fieldBuilder.build(name))
    }
}

// --------------------------------- IntegerField ---------------------------------

/**
 * Builder for [IntegerField]
 * @see IntegerField
 */
class IntegerFieldBuilder : FieldBuilder() {

    override fun build(name: String): IntegerField {
        return IntegerField(name, primaryKey, index, unique, nullable)
    }
}

/**
 * Interface for creating [IntegerField] using DSL
 *
 * Example usage:
 * ```
 * integerField("fieldName") {
 *     primaryKey = true
 * }
 * ```
 */
interface IntegerFieldBuilderDSL : FieldBuilderDSL {

    fun integerField(name: String, setup: IntegerFieldBuilder.() -> Unit = {}) {
        val fieldBuilder = IntegerFieldBuilder()
        fieldBuilder.setup()
        addField(fieldBuilder.build(name))
    }
}

// --------------------------------- CharField ------------------------------------

/**
 * Builder for [CharField]
 * @see CharField
 */
class CharFieldBuilder : FieldBuilder() {

    var maxLength: Int by Delegates.notNull()

    override fun build(name: String): CharField {
        return CharField(name, primaryKey, index, unique, nullable, maxLength)
    }
}

/**
 * Interface for creating [CharField] using DSL
 *
 * Example usage:
 * ```
 * charField("fieldName") {
 *     maxLength = 150
 *     primaryKey = true
 * }
 * ```
 */
interface CharFieldBuilderDSL : FieldBuilderDSL {

    fun charField(name: String, setup: CharFieldBuilder.() -> Unit) {
        val fieldBuilder = CharFieldBuilder()
        fieldBuilder.setup()
        addField(fieldBuilder.build(name))
    }
}

/**
 * Barrel interface for implementing all FieldsDSL
 */
interface AllFieldsBuilderDSL :
    AutoFieldBuilderDSL,
    IntegerFieldBuilderDSL,
    CharFieldBuilderDSL