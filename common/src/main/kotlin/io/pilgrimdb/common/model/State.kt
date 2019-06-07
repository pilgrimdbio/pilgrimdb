package io.pilgrimdb.common.model

data class ProjectModelEntry(val packageName: String, val name: String)

class ProjectState(val models: MutableMap<ProjectModelEntry, ModelState> = mutableMapOf()) {

    fun addModel(packageName: String, model: ModelState) {
        models[ProjectModelEntry(packageName, model.name)] = model
    }

    fun getModel(packageName: String, model: String): ModelState {
        return models[ProjectModelEntry(packageName, model)]!!
    }

    fun mergeState(toMerge: ProjectState): ProjectState {
        val out = ProjectState(this.models.toMutableMap())
        for (model in toMerge.models){
            out.models[model.key] = model.value
        }
        return out
    }
}

class ModelState(val name: String, val fields: MutableList<Field> = mutableListOf()) {

    fun getField(fieldName: String): Field {
        return fields.filter { it.name == fieldName }.first()
    }
}
