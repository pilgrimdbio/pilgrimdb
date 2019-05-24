package io.pilgrimdb.common.migrations.builders

import io.pilgrimdb.common.migrations.operations.CreateModel
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MigrationBuilderTest {

    @Test
    fun testBuild() {

        val migration = migration {
            createModel("tableName") {
            }
        }

        migration.operations.size shouldEqualTo 1
        migration.operations[0] shouldBeInstanceOf CreateModel::class
        (migration.operations[0] as CreateModel).name shouldEqual "tableName"
    }
}
