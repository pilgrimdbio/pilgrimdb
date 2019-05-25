package io.pilgrim.common.migrations

import io.pilgrim.common.tests.PostgresTest
import io.pilgrim.common.tests.extensions.DatabaseConnectionExtension
import io.pilgrim.common.tests.extensions.PostgresConnectionExtension
import io.pilgrimdb.common.db.base.Connection
import io.pilgrimdb.common.migrations.MigrationRecorder
import kotliquery.Session
import kotliquery.queryOf
import org.amshove.kluent.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@PostgresTest
class MigrationRecorderTest {

    @Nested
    @ExtendWith(PostgresConnectionExtension::class)
    @PostgresTest
    class `Postgres Tests For Migration Recorder` {

        @Test
        fun testCreateModel(connection: Connection, session: Session) {
            // Should not exist before
            session.run(
                queryOf("SELECT COUNT(*) FROM pg_catalog.pg_tables WHERE tablename='pilgrim_migrations'")
                    .map { it.int(1) }.asSingle
            ) shouldBe 0

            val recorder = MigrationRecorder(connection)
            recorder.ensureSchema()

            // Should be created
            session.run(
                queryOf("SELECT COUNT(*) FROM pg_catalog.pg_tables WHERE tablename='pilgrim_migrations'")
                    .map { it.int(1) }.asSingle
            ) shouldBe 1
        }
    }
}