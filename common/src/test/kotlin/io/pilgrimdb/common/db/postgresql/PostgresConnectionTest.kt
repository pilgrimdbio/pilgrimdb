package io.pilgrimdb.common.db.postgresql

import io.pilgrimdb.common.config.DatabaseConfig
import io.pilgrimdb.common.model.AutoField
import io.pilgrimdb.common.model.CharField
import io.pilgrimdb.common.model.IntegerField
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostgresConnectionTest {

    val connection = PostgresConnection(DatabaseConfig("test", "test", "test"))

    @Nested
    inner class `Test data types mappings` {

        @Test
        fun testAutoField() {
            connection.dataTypes[AutoField::class] shouldEqual "serial"
        }

        @Test
        fun testCharField() {
            connection.dataTypes[CharField::class] shouldEqual "varchar(#{maxLength})"
        }

        @Test
        fun testIntegerFIeld() {
            connection.dataTypes[IntegerField::class] shouldEqual "integer"
        }
    }
}