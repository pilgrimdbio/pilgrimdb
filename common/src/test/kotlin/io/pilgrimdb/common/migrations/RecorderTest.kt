package io.pilgrimdb.common.migrations

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.pilgrimdb.common.db.base.Connection
import io.pilgrimdb.common.db.base.TableInfo
import org.amshove.kluent.shouldContain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecorderTest {

    private val connection = mockk<Connection>(relaxed = true)
    private val recorder = Recorder(connection)

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

    @Test
    fun `apply entry`() {
        recorder.recordApplied("test.package", "migrationName")
        verify { connection.execute("INSERT INTO pilgrim_migrations (package, name) VALUES ('test.package', 'migrationName')") }
    }

    @Test
    fun `unapply entry`() {
        recorder.recordUnapplied("test.package", "migrationName")
        verify { connection.execute("DELETE FROM pilgrim_migrations WHERE package='test.package' AND name='migrationName'") }
    }

    @Test
    fun `get all applied migrations`() {
        every { connection.query<Pair<String, String>>(any(), any()) } returns listOf(Pair("package", "name"))
        recorder.getAppliedMigrations() shouldContain ("package" to "name")
        verify { connection.query<Pair<String, String>>("SELECT package, name FROM pilgrim_migrations", any()) }
    }
}