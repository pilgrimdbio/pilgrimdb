package io.pilgrim.common.tests.extensions

import io.pilgrimdb.common.config.DatabaseConfig
import io.pilgrimdb.common.db.base.Connection
import io.pilgrimdb.common.db.postgresql.PostgresConnection
import kotliquery.Session
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

abstract class DatabaseConnectionExtension : BeforeAllCallback, BeforeEachCallback, AfterAllCallback, AfterEachCallback,
    ParameterResolver {

    val connection = createConnection()

    abstract fun createConnection(): Connection

    override fun beforeAll(context: ExtensionContext?) {
        connection.connect()
    }

    override fun beforeEach(context: ExtensionContext?) {
        connection.session!!.connection.begin()
    }

    override fun afterEach(context: ExtensionContext?) {
        connection.session!!.connection.rollback()
    }

    override fun afterAll(context: ExtensionContext?) {
        connection.disconnect()
    }

    override fun supportsParameter(parameterContext: ParameterContext?, extensionContext: ExtensionContext?): Boolean {
        return when (parameterContext!!.parameter.type) {
            Connection::class.java -> true
            Session::class.java -> true
            else -> false
        }
    }

    override fun resolveParameter(parameterContext: ParameterContext?, extensionContext: ExtensionContext?): Any {
        return when (parameterContext!!.parameter.type) {
            Connection::class.java -> connection
            Session::class.java -> connection.session!!
            else -> false
        }
    }
}


class PostgresConnectionExtension: DatabaseConnectionExtension() {

    override fun createConnection(): Connection {
        val postgresConfig: DatabaseConfig = DatabaseConfig(
            System.getProperty("io.pilgrim.common.test.postgres.user"),
            System.getProperty("io.pilgrim.common.test.postgres.password"),
            System.getProperty("io.pilgrim.common.test.postgres.url")
        )

        return PostgresConnection(postgresConfig)
    }
}