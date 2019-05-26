package io.pilgrimdb.common.migrations

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.pilgrimdb.common.db.base.Connection
import io.pilgrimdb.common.db.base.TableInfo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MigrationRecorderTest {

    private val connection = mockk<Connection>(relaxed = true)
    private val recorder = MigrationRecorder(connection)

    @AfterEach
    fun cleanUp() {
        clearAllMocks()
    }

    @Test
    fun `should create the model if it doesnt exists`() {
        every { connection.introspection.getTableNames() } returns listOf()
        recorder.ensureSchema()
        verify { connection.schemaEditor.createModel(any()) }
    }

    @Test
    fun `should not create the model if it exists`() {
        every { connection.introspection.getTableNames() } returns listOf(TableInfo("pilgrim_migrations", "t"))
        recorder.ensureSchema()
        verify(exactly = 0) { connection.schemaEditor.createModel(any()) }
    }
}