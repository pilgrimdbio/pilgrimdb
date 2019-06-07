package io.pilgrimdb.generator.state

import io.pilgrimdb.common.annotations.PilgrimModel
import io.pilgrimdb.common.model.ProjectState
import io.pilgrimdb.generator.state.adapters.ExposedStateAdapter
import io.pilgrimdb.generator.state.adapters.StateAdapter
import kotlin.reflect.KClass
import org.reflections.Reflections

class StateProvider {

    private val packageName: String
    private val adapters: List<StateAdapter>

    constructor(packageName: String) : this(packageName, listOf<StateAdapter>(ExposedStateAdapter))

    constructor(packageName: String, adapters: List<StateAdapter>) {
        this.packageName = packageName
        this.adapters = adapters
    }

    fun getState(): ProjectState {
        val reflections = Reflections(packageName)
        val classes = reflections.getTypesAnnotatedWith(PilgrimModel::class.java).map { it.kotlin }
        val state = ProjectState()

        classes.map { k -> getPackage(k) to adapters.first { it.isSupported(k) }.convertModel(k) }
            .forEach { state.addModel(it.first, it.second) }

        return state
    }

    private fun getPackage(klass: KClass<out Any>): String = klass.java.`package`.name
}