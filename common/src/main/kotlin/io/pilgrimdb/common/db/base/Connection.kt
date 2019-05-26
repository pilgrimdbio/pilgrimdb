package io.pilgrimdb.common.db.base

import io.pilgrimdb.common.config.DatabaseConfig
import io.pilgrimdb.common.model.Field
import kotlin.reflect.KClass
import kotliquery.Row
import kotliquery.Session
import kotliquery.queryOf
import kotliquery.sessionOf
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

abstract class Connection(private val config: DatabaseConfig) {

    abstract val dataTypes: Map<KClass<out Field>, String>

    abstract val operations: Operations

    abstract val schemaEditor: SchemaEditor

    abstract val introspection: Introspection

    var session: Session? = null

    fun connect() {
        logger.debug { "Connecting to db" }
        session = sessionOf(config.url, config.username, config.password)
    }

    fun disconnect() {
        logger.debug { "Disconnecting from db" }
        session!!.close()
    }

    fun execute(sql: String) {
        logger.debug { "Sql: $sql" }
        val response = session!!.run(queryOf(sql).asExecute)
    }

    fun <T> query(sql: String, map: (Row) -> T?): List<T> {
        logger.debug { "Sql: $sql" }
        return session!!.run(queryOf(sql).map(map).asList)
    }
}