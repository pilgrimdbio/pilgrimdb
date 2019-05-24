package io.pilgrimdb.sample.exposed

import io.pilgrimdb.common.annotations.PilgrimModel
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
