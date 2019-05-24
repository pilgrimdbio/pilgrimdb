package io.pilgrimdb.common.operations

import io.pilgrimdb.common.model.ProjectState

/**
 * Base class for all migration operations
 */
abstract class Operation {

    /**
     * Applies the operation to a [state]
     */
    abstract fun stateForwards(state: ProjectState)
}

