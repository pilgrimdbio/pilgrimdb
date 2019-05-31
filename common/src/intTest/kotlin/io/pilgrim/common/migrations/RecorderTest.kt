package io.pilgrim.common.migrations

import io.pilgrim.common.tests.PostgresTest
import io.pilgrim.common.tests.extensions.PostgresConnectionExtension
import io.pilgrimdb.common.db.base.Connection
import io.pilgrimdb.common.migrations.Recorder
import kotliquery.Session
import kotliquery.queryOf
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@PostgresTest
class RecorderTest {

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

            val recorder = Recorder(connection)
            recorder.ensureSchema()

            // Should be created
            session.run(
                queryOf("SELECT COUNT(*) FROM pg_catalog.pg_tables WHERE tablename='pilgrim_migrations'")
                    .map { it.int(1) }.asSingle
            ) shouldBe 1
        }

        @Test
        fun `test record apply and unapply`(connection: Connection, session: Session) {
            val recorder = Recorder(connection)
            recorder.ensureSchema()

            recorder.recordApplied("test.package", "migrationName")
            // Should be created
            session.run(
                queryOf("SELECT COUNT(*) FROM pilgrim_migrations WHERE package='test.package'")
                    .map { it.int(1) }.asSingle
            ) shouldBe 1

            recorder.recordUnapplied("test.package", "migrationName")
            // Should be deleted
            session.run(
                queryOf("SELECT COUNT(*) FROM pilgrim_migrations WHERE package='test.package'")
                    .map { it.int(1) }.asSingle
            ) shouldBe 0
        }

        @Test
        fun `test get all applied migrations`(connection: Connection, session: Session) {
            val recorder = Recorder(connection)
            recorder.ensureSchema()

            recorder.recordApplied("test.package1", "migrationName1")
            recorder.recordApplied("test.package2", "migrationName2")

            val response = recorder.getAppliedMigrations()

            response.size shouldEqualTo 2
            response shouldContain ("test.package1" to "migrationName1")
            response shouldContain ("test.package2" to "migrationName2")
        }
    }
}