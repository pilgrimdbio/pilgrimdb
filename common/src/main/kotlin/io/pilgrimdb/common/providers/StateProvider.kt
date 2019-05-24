package io.pilgrimdb.common.providers

import io.pilgrimdb.common.model.ProjectState

interface StateProvider {

    fun getState(): ProjectState
}