package io.pilgrimdb.common.db.base

abstract class Operations(val connection: Connection) {

    abstract fun quote_name(name: String): String
}