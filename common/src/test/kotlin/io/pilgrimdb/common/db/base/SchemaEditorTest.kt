package io.pilgrimdb.common.db.base

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.pilgrimdb.common.model.Field
import io.pilgrimdb.common.model.ModelState
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SchemaEditorTest {

    class ConcreteSchemaEditor(connection: Connection) : SchemaEditor(connection)

    val connection = mockk<Connection>(relaxed = true)
    val schemaEditor = ConcreteSchemaEditor(connection)

    @BeforeEach
    fun setUp() {
        every { connection.operations.quoteName(any()) } answers { "\"${invocation.args[0]}\"" }
    }

    @AfterEach
    fun cleanUp() {
        clearAllMocks()
    }

    @Nested
    inner class CreateModelTest {

        val field = mockk<Field>(relaxed = true)
        val model = ModelState("test", fields = mutableListOf(field))

        @BeforeEach
        fun initializeField() {
            every { field.name } returns "name"
            every { field.dbParameters(any()) } returns ColumnDBParameters("testsql", null)
        }

        @Test
        fun `simple create model`() {
            schemaEditor.createModel(model)
            verify { connection.execute("CREATE TABLE \"test\" (\"name\" testsql NOT NULL)") }
        }

        @Test
        fun `no db parameters for field, it should skip it`() {
            every { field.dbParameters(any()) } returns ColumnDBParameters(null, null)
            schemaEditor.createModel(model)
            verify { connection.execute("CREATE TABLE \"test\" ()") }
        }

        @Test
        fun `field is primary key`() {
            every { field.primaryKey } returns true
            schemaEditor.createModel(model)
            verify { connection.execute("CREATE TABLE \"test\" (\"name\" testsql NOT NULL PRIMARY KEY)") }
        }

        @Test
        fun `field is unique`() {
            every { field.unique } returns true
            schemaEditor.createModel(model)
            verify { connection.execute("CREATE TABLE \"test\" (\"name\" testsql NOT NULL UNIQUE)") }
        }

        @Test
        fun `field is nullable`() {
            every { field.nullable } returns true
            schemaEditor.createModel(model)
            verify { connection.execute("CREATE TABLE \"test\" (\"name\" testsql NULL)") }
        }
    }
}