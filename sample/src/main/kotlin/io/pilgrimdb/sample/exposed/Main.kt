package io.pilgrimdb.sample.exposed

import io.pilgrimdb.common.annotations.PilgrimModel
import org.jetbrains.exposed.sql.Table

@PilgrimModel
object Users : Table() {
    val id = integer("id").primaryKey()
    val name = varchar("name", length = 50)
}

@PilgrimModel
object Cities : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 50)
}

/*
@PilgrimModel
object Countries : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 50)
}
*/
