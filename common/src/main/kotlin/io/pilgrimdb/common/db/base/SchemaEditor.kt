package io.pilgrimdb.common.db.base

import io.pilgrimdb.common.extensions.substitute
import io.pilgrimdb.common.migrations.ModelState
import io.pilgrimdb.common.model.Field

data class ColumnDBParameters(val type: String?, val check: String?)

data class ColumnSql(val sql: String, val params: List<String> = listOf())

abstract class SchemaEditor(val connection: Connection) {

    protected open val sqlCreateTable = "CREATE TABLE #{table} (#{definition})"

    protected open val sqlCreateColumn = "ALTER TABLE #{table} ADD COLUMN #{column} #{definition}"

    fun createModel(model: ModelState) {
        val columnSqls = mutableListOf<String>()
        val params = mutableListOf<String>()

        for (field in model.fields) {
            val columnSql = columnSql(model, field)
            columnSqls.add("${connection.operations.quote_name(field.name)} ${columnSql?.sql}")
        }

        var sql = sqlCreateTable.substitute(
            mapOf(
                "table" to connection.operations.quote_name(model.name),
                "definition" to columnSqls.joinToString(", ")
            )
        )
        connection.execute(sql)
    }

    private fun columnSql(model: ModelState, field: Field): ColumnSql? {
        var dbParams = field.dbParameters(connection)

        // Check for fields that arent columns (e.g. M2M)
        if (dbParams.type == null) {
            return null
        }

        var sql = dbParams.type
        val params = mutableListOf<String>()
        var isNullable = field.nullable

        sql = if (field.nullable) {
            "$sql NULL"
        } else {
            "$sql NOT NULL"
        }
        // Primary key/unique outputs
        if (field.primaryKey) {
            sql = "$sql PRIMARY KEY"
        } else if (field.unique) {
            sql = "$sql UNIQUE"
        }
        return ColumnSql(sql, params)
    }
}