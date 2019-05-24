package io.pilgrimdb.common.model

import io.pilgrimdb.common.db.base.ColumnDBParameters
import io.pilgrimdb.common.db.base.Connection
import io.pilgrimdb.common.extensions.substitute

sealed class Field(
    val name: String,
    val primaryKey: Boolean,
    val index: Boolean,
    val unique: Boolean,
    val nullable: Boolean
) {
    open fun dbParameters(connection: Connection): ColumnDBParameters {
        return ColumnDBParameters(dbType(connection), null)
    }

    open fun dbType(connection: Connection): String? {
        return connection.dataTypes[this::class]
    }
}

class AutoField(
    name: String,
    primaryKey: Boolean,
    index: Boolean,
    unique: Boolean,
    nullable: Boolean
) : Field(name, primaryKey, index, unique, nullable)

class IntegerField(
    name: String,
    primaryKey: Boolean,
    index: Boolean,
    unique: Boolean,
    nullable: Boolean
) : Field(name, primaryKey, index, unique, nullable)

class CharField(
    name: String,
    primaryKey: Boolean,
    index: Boolean,
    unique: Boolean,
    nullable: Boolean,
    val maxLength: Int
) : Field(name, primaryKey, index, unique, nullable) {

    override fun dbType(connection: Connection): String? {
        return super.dbType(connection)?.substitute(mapOf("maxLength" to maxLength.toString()))
    }
}
