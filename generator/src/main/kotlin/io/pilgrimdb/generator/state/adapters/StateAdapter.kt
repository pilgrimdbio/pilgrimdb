package io.pilgrimdb.generator.state.adapters

import io.pilgrimdb.common.model.ModelState
import kotlin.reflect.KClass

interface StateAdapter {

    fun convertModel(klass: KClass<out Any>): ModelState

    fun isSupported(klass: KClass<out Any>): Boolean
}