package io.pilgrimdb.common.model

data class ProjectModelEntry(val packageName: String, val name: String)

class ProjectState(val models: MutableMap<ProjectModelEntry, ModelState> = mutableMapOf()) {

    fun addModel(packageName: String, model: ModelState) {
        models[ProjectModelEntry(packageName, model.name)] = model
    }

    fun getModel(packageName: String, model: String): ModelState {
        return models[ProjectModelEntry(packageName, model)]!!
    }
}

class ModelState(val name: String, val fields: MutableList<Field> = mutableListOf()) {

    fun getField(fieldName: String): Field {
        return fields.filter { it.name == fieldName }.first()
    }
}
