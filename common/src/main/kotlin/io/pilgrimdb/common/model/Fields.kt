package io.pilgrimdb.common.model

import io.pilgrimdb.common.operations.AddFieldBuilder
import io.pilgrimdb.common.operations.CreateModelBuilder
import kotlin.properties.Delegates

sealed class Field(
    val name: String,
    val primaryKey: Boolean,
    val index: Boolean,
    val unique: Boolean,
    val nullable: Boolean
)

sealed class FieldBuilder {

    var name: String by Delegates.notNull()

    var primaryKey = false

    var index = false

    var unique = false

    var nullable = false

    abstract fun build(name: String): Field
}

class AutoField(
    name: String,
    primaryKey: Boolean,
    index: Boolean,
    unique: Boolean,
    nullable: Boolean
) : Field(name, primaryKey, index, unique, nullable)

class AutoFieldBuilder : FieldBuilder() {

    override fun build(name: String): AutoField {
        return AutoField(name, primaryKey, index, unique, nullable)
    }
}

fun CreateModelBuilder.autoField(name: String, setup: AutoFieldBuilder.() -> Unit = {}) {
    val fieldBuilder = AutoFieldBuilder()
    fieldBuilder.setup()
    val field = fieldBuilder.build(name)
    fields += field
}

class IntegerField(
    name: String,
    primaryKey: Boolean,
    index: Boolean,
    unique: Boolean,
    nullable: Boolean
) : Field(name, primaryKey, index, unique, nullable)

class IntegerFieldBuilder : FieldBuilder() {

    override fun build(name: String): IntegerField {
        return IntegerField(name, primaryKey, index, unique, nullable)
    }
}

fun CreateModelBuilder.integerField(name: String, setup: IntegerFieldBuilder.() -> Unit = {}) {
    val fieldBuilder = IntegerFieldBuilder()
    fieldBuilder.setup()
    val field = fieldBuilder.build(name)
    fields += field
}

fun AddFieldBuilder.integerField(name: String, setup: IntegerFieldBuilder.() -> Unit = {}) {
    val fieldBuilder = IntegerFieldBuilder()
    fieldBuilder.setup()
    field = fieldBuilder.build(name)
}

class CharField(
    name: String,
    primaryKey: Boolean,
    index: Boolean,
    unique: Boolean,
    nullable: Boolean,
    val maxLength: Int
) : Field(name, primaryKey, index, unique, nullable)

class CharFieldBuilder : FieldBuilder() {

    var maxLength: Int by Delegates.notNull()

    override fun build(name: String): CharField {
        return CharField(name, primaryKey, index, unique, nullable, maxLength)
    }
}

fun CreateModelBuilder.charField(name: String, setup: CharFieldBuilder.() -> Unit = {}) {
    val fieldBuilder = CharFieldBuilder()
    fieldBuilder.setup()
    val field = fieldBuilder.build(name)
    fields += field
}