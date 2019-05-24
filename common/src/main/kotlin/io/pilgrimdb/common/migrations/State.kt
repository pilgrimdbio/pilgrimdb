package io.pilgrimdb.common.migrations

import io.pilgrimdb.common.model.Field

class ProjectState(val models: MutableMap<String, ModelState> = mutableMapOf()) {

    fun addModel(model: ModelState) {
        models[model.name] = model
    }

    fun getModel(table: String): ModelState {
        return requireNotNull(models[table])
    }
}

class ModelState(val name: String, val fields: MutableList<Field> = mutableListOf()) {

    fun getField(fieldName: String): Field {
        return fields.filter { it.name == fieldName }.first()
    }
}
