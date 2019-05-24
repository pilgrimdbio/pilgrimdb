package io.pilgrimdb.generator.exposed

import io.pilgrimdb.common.annotations.PilgrimModel
import org.jetbrains.exposed.sql.Table

@PilgrimModel
object Users : Table() {
    val id = varchar("id", 10).primaryKey() // Column<String>
    val name = varchar("name", length = 50) // Column<String>
}

@PilgrimModel
object Cities : Table() {
    val id = integer("id").autoIncrement().primaryKey() // Column<Int>
    val name = varchar("name", 50) // Column<String>
}

fun main() {
    val state = ExposedStateProvider("net.buluba.pilgrim.exposed").getState()
}
