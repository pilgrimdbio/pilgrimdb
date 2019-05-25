package io.pilgrimdb.common.db.base

data class TableInfo(val name: String, val type: String)

abstract class Introspection(val connection: Connection) {

    abstract fun getTableNames(): List<TableInfo>
}