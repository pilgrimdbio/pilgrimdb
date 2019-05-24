package io.pilgrimdb.common.db.postgresql

import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import io.pilgrimdb.common.config.DatabaseConfig
import io.pilgrimdb.common.migrations.ModelState
import io.pilgrimdb.common.model.AutoField
import io.pilgrimdb.common.model.CharField
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PostgresSchemaEditorTest {

    private val databaseConnection = spyk(PostgresConnection(DatabaseConfig("t", "t", "t")))
    private val schemaEditor = PostgresSchemaEditor(databaseConnection)

    @BeforeEach
    fun init() {
        every { databaseConnection.execute(any()) } returns Unit
    }

    @Nested
    inner class CreateModelTest {
        @Test
        fun testSimple() {
            val model = ModelState(
                "test",
                mutableListOf(
                    AutoField(
                        "id",
                        primaryKey = true,
                        index = false,
                        unique = true,
                        nullable = false
                    ),
                    CharField(
                        "name",
                        primaryKey = false,
                        index = false,
                        unique = true,
                        nullable = false,
                        maxLength = 100
                    )
                )
            )
            schemaEditor.createModel(model)
            verify { databaseConnection.execute("CREATE TABLE \"test\" (\"id\" serial NOT NULL PRIMARY KEY, \"name\" varchar(100) NOT NULL UNIQUE)") }
        }
    }
}