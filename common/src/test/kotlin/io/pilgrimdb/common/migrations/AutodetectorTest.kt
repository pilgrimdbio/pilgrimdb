package io.pilgrimdb.common.migrations

import io.pilgrimdb.common.migrations.operations.CreateModel
import io.pilgrimdb.common.model.AutoField
import io.pilgrimdb.common.model.ModelState
import io.pilgrimdb.common.model.ProjectModelEntry
import io.pilgrimdb.common.model.ProjectState
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveKey
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AutodetectorTest {

    @Nested
    inner class CreateModel {
        @Test
        fun testAdded() {
            val fromState = ProjectState()
            val toState = ProjectState(
                models = mutableMapOf(
                    ProjectModelEntry("package", "test") to ModelState(
                        "test",
                        mutableListOf(
                            AutoField("field1", false, false, false, false)
                        )
                    )
                )
            )
            val operations = Autodetector(fromState, toState).changes()

            operations shouldHaveKey "package"
            operations["package"]!! shouldContain CreateModel(
                "test",
                toState.models[ProjectModelEntry("package", "test")]?.fields!!
            )
        }

        @Test
        fun testNoChanges() {
            val toState = ProjectState(
                models = mutableMapOf(
                    ProjectModelEntry("package", "test") to ModelState(
                        "test",
                        mutableListOf(
                            AutoField("field1", false, false, false, false)
                        )
                    )
                )
            )
            val operations = Autodetector(toState, toState).changes()

            operations.shouldBeEmpty()
        }
    }
}
