package io.pilgrimdb.common.migrations

import io.pilgrimdb.common.migrations.operations.CreateModel
import io.pilgrimdb.common.model.AutoField
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldContain
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MigrationAutodetectorTest {

    @Nested
    inner class CreateModel {
        @Test
        fun testAdded() {
            val fromState = ProjectState()
            val toState = ProjectState(
                models = mutableMapOf(
                    "test" to ModelState(
                        "test",
                        mutableListOf(
                            AutoField("field1", false, false, false, false)
                        )
                    )
                )
            )
            val operations = MigrationAutodetector(fromState, toState).changes()

            operations shouldContain CreateModel(
                "test",
                toState.models["test"]?.fields!!
            )
        }

        @Test
        fun testNoChanges() {
            val toState = ProjectState(
                models = mutableMapOf(
                    "test" to ModelState(
                        "test",
                        mutableListOf(
                            AutoField("field1", false, false, false, false)
                        )
                    )
                )
            )
            val operations = MigrationAutodetector(toState, toState).changes()

            operations.shouldBeEmpty()
        }
    }
}
