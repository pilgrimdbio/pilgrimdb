package io.pilgrimdb.common.db.base

abstract class Operations(val connection: Connection) {

    abstract fun quoteName(name: String): String
}