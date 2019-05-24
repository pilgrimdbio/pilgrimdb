package io.pilgrimdb.generator.exposed

import io.pilgrimdb.common.annotations.PilgrimModel
import io.pilgrimdb.common.model.ProjectState
import io.pilgrimdb.common.providers.StateProvider
import org.jetbrains.exposed.sql.Table
import org.reflections.Reflections
import kotlin.reflect.full.isSubclassOf

class ExposedStateProvider(private val packageName: String) : StateProvider {

    override fun getState(): ProjectState {
        val reflections = Reflections(packageName)
        val classes = reflections.getTypesAnnotatedWith(PilgrimModel::class.java)
        val state = ProjectState()

        for (table in classes.map { it.kotlin }) {
            if (!table.isSubclassOf(Table::class)) {
                println("not a subclass")
                continue
            }

            val instance = table.objectInstance as Table
            // val tableState = ModelState(instance.tableName)
            // tableState.operations.addAll(instance.columns.map {
            //     getColumnType(
            //         it
            //     )
            // })
            // state.tables.put(tableState.name, tableState)
        }
        return state
    }

    // private fun getColumnType(column: Column<*>): Field {
    //     val field = when (column.columnType) {
    //         is AutoIncColumnType -> (column.columnType as AutoIncColumnType).delegate
    //         else -> column.columnType
    //     }
    //
    //     val type = when (columnType) {
    //         is VarCharColumnType -> Varchar((column.columnType as VarCharColumnType).colLength)
    //         is IntegerColumnType -> Integer()
    //         else -> throw IllegalArgumentException("sdf")
    //     }
    //
    //     return net.buluba.pilgrim.dsl.Column(column.name, type)
    // }
}