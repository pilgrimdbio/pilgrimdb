package io.pilgrimdb.common.db.postgresql

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.pilgrimdb.common.db.base.TableInfo
import org.amshove.kluent.shouldContain
import org.junit.jupiter.api.Test

class PostgresIntrospectionTest {

    val connection = mockk<PostgresConnection>()
    val introspection = PostgresIntrospection(connection)

    @Test
    fun testGetTableNames() {
        every { connection.query<TableInfo>(any(), any()) } returns listOf(TableInfo("test", "type"))

        introspection.getTableNames() shouldContain TableInfo("test", "type")
        val query =
            """
        |    SELECT c.relname,
        |    CASE WHEN FALSE THEN 'p' WHEN c.relkind IN ('m', 'v') THEN 'v' ELSE 't' END
        |    FROM pg_catalog.pg_class c
        |    LEFT JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace
        |    WHERE c.relkind IN ('f', 'm', 'p', 'r', 'v')
        |        AND n.nspname NOT IN ('pg_catalog', 'pg_toast')
        |        AND pg_catalog.pg_table_is_visible(c.oid)
        """.trimMargin()
        verify { connection.query<TableInfo>(query, any()) }
    }
}