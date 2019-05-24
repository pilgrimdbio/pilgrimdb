package io.pilgrimdb.common.migrations.providers

import io.pilgrimdb.common.model.ProjectState

interface StateProvider {

    fun getState(): ProjectState
}