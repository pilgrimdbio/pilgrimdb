package io.pilgrimdb.common.db.postgresql

import io.pilgrimdb.common.config.DatabaseConfig
import io.pilgrimdb.common.db.base.Connection
import io.pilgrimdb.common.db.base.Introspection
import io.pilgrimdb.common.model.AutoField
import io.pilgrimdb.common.model.CharField
import io.pilgrimdb.common.model.Field
import io.pilgrimdb.common.model.IntegerField
import kotlin.reflect.KClass

class PostgresConnection(config: DatabaseConfig) : Connection(config) {

    override val schemaEditor = PostgresSchemaEditor(this)

    override val operations = PostgresOperations(this)

    override val introspection: Introspection = PostgresIntrospection(this)

    override val dataTypes: Map<KClass<out Field>, String> = mapOf(
        AutoField::class to "serial",
        CharField::class to "varchar(#{maxLength})",
        IntegerField::class to "integer"
    )
}
