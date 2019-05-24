package io.pilgrimdb.common.db.postgresql

import io.pilgrimdb.common.db.base.Connection
import io.pilgrimdb.common.db.base.Operations

class PostgresOperations(connection: Connection) : Operations(connection) {

    override fun quote_name(name: String): String {
        if (name.startsWith("\"") and name.endsWith("\"")) {
            // Already quoted
            return name
        }
        return "\"$name\""
    }
}