package io.pilgrimdb.common.migrations.builders

import io.pilgrimdb.common.migrations.operations.CreateModel
import io.pilgrimdb.common.migrations.operations.MigrationIndex
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MigrationBuilderTest {

    @Test
    fun testBuild() {

        val migration = migration("package", "name") {
            dependsOn("dependPackage", "dependMigration")
            createModel("tableName") {
            }
        }

        migration.packageName shouldBeEqualTo "package"
        migration.migrationName shouldBeEqualTo "name"
        migration.operations.size shouldEqualTo 1
        migration.operations[0] shouldBeInstanceOf CreateModel::class
        (migration.operations[0] as CreateModel).name shouldEqual "tableName"
        migration.dependencies.size shouldEqualTo 1
        migration.dependencies[0] shouldEqual MigrationIndex("dependPackage", "dependMigration")
    }
}
