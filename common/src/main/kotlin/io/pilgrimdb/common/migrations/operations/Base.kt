package io.pilgrimdb.common.migrations.operations

import io.pilgrimdb.common.migrations.ProjectState

/**
 * Base class for all migration operations
 */
abstract class Operation {

    /**
     * Applies the operation to a [state]
     */
    abstract fun stateForwards(state: ProjectState)
}
