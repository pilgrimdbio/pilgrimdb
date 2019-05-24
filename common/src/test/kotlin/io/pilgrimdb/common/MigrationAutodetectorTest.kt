package io.pilgrimdb.common

import io.pilgrimdb.common.model.AutoField
import io.pilgrimdb.common.model.ModelState
import io.pilgrimdb.common.model.ProjectState
import io.pilgrimdb.common.operations.CreateModel
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldContain
import org.junit.Test

class MigrationAutodetectorTest {

    @Test
    fun createModelTest() {
        val fromState = ProjectState()
        val toState = ProjectState(
            models = mutableMapOf(
                "test" to ModelState(
                    "test", mutableListOf(
                        AutoField("field1", false, false, false, false)
                    )
                )
            )
        )
        val operations = MigrationAutodetector(fromState, toState).changes()

        operations shouldContain CreateModel("test", toState.models["test"]?.fields!!)
    }

    @Test
    fun createModelTest_NoChanges() {
        val toState = ProjectState(
            models = mutableMapOf(
                "test" to ModelState(
                    "test", mutableListOf(
                        AutoField("field1", false, false, false, false)
                    )
                )
            )
        )
        val operations = MigrationAutodetector(toState, toState).changes()

        operations.shouldBeEmpty()
    }
}
