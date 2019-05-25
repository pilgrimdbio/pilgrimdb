package io.pilgrimdb.common.db.postgresql

import io.pilgrimdb.common.db.base.Connection
import io.pilgrimdb.common.db.base.Introspection
import io.pilgrimdb.common.db.base.TableInfo

class PostgresIntrospection(connection: Connection) : Introspection(connection) {

    override fun getTableNames(): List<TableInfo> {
        val query = """
        |    SELECT c.relname,
        |    CASE WHEN FALSE THEN 'p' WHEN c.relkind IN ('m', 'v') THEN 'v' ELSE 't' END
        |    FROM pg_catalog.pg_class c
        |    LEFT JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace
        |    WHERE c.relkind IN ('f', 'm', 'p', 'r', 'v')
        |        AND n.nspname NOT IN ('pg_catalog', 'pg_toast')
        |        AND pg_catalog.pg_table_is_visible(c.oid)
        """.trimMargin()
        return connection.query(query) { TableInfo(it.string(1), it.string(2)) }
    }
}