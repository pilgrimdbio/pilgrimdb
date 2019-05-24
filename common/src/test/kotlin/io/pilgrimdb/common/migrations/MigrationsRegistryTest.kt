package io.pilgrimdb.common.migrations

import io.pilgrimdb.common.migrations.operations.Migration
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldHaveKey
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MigrationsRegistryTest {

    @Test
    fun testRegisterMigration() {
        val migration = Migration("package", "name")
        MigrationsRegistry.registerMigration("package", migration)

        val registeredMigrations = MigrationsRegistry.getAllMigrations()

        registeredMigrations.size shouldEqualTo 1
        registeredMigrations shouldHaveKey "package"
        registeredMigrations["package"]!!.size shouldEqual 1
        registeredMigrations["package"]!![0] shouldBe migration
    }
}