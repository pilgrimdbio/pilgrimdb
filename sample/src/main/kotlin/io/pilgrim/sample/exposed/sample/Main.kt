package io.pilgrim.sample.exposed.sample

import io.pilgrimdb.common.PilgrimModel
import org.jetbrains.exposed.sql.Table

@PilgrimModel
object Users : Table() {
    val id = Users.varchar("id", 10).primaryKey() // Column<String>
    val name = Users.varchar("name", length = 50) // Column<String>
}

@PilgrimModel
object Cities : Table() {
    val id = Cities.integer("id").autoIncrement().primaryKey() // Column<Int>
    val name = Cities.varchar("name", 50) // Column<String>
}

fun main() {
    val a = 5
}
