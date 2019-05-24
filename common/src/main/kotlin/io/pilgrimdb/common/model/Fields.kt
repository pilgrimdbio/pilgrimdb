package io.pilgrimdb.common.model

sealed class Field(
    val name: String,
    val primaryKey: Boolean,
    val index: Boolean,
    val unique: Boolean,
    val nullable: Boolean
)

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
) : Field(name, primaryKey, index, unique, nullable)
