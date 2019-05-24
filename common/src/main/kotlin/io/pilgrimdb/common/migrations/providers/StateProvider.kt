package io.pilgrimdb.common.migrations.providers

import io.pilgrimdb.common.migrations.ProjectState

interface StateProvider {

    fun getState(): ProjectState
}