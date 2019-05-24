package io.pilgrimdb.common.db.postgresql

import io.pilgrimdb.common.db.base.Connection
import io.pilgrimdb.common.db.base.SchemaEditor

open class PostgresSchemaEditor(connection: Connection) : SchemaEditor(connection)