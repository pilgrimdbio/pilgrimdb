package io.pilgrimdb.common.db.base

import io.pilgrimdb.common.config.DatabaseConfig
import io.pilgrimdb.common.model.Field
import kotlin.reflect.KClass
import kotliquery.Session
import kotliquery.queryOf
import kotliquery.sessionOf

abstract class Connection(val config: DatabaseConfig) {

    abstract val dataTypes: Map<KClass<out Field>, String>

    abstract val operations: Operations

    abstract val schemaEditor: SchemaEditor

    private var session: Session? = null

    fun connect() {
        session = sessionOf(config.url, config.username, config.password)
    }

    fun execute(sql: String) {
        val response = session!!.run(queryOf(sql).asExecute)
    }
}