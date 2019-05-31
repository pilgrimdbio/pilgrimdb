package io.pilgrimdb.generator.exposed

import io.pilgrimdb.common.annotations.PilgrimModel
import io.pilgrimdb.common.migrations.providers.StateProvider
import io.pilgrimdb.common.model.AutoField
import io.pilgrimdb.common.model.CharField
import io.pilgrimdb.common.model.Field
import io.pilgrimdb.common.model.IntegerField
import io.pilgrimdb.common.model.ModelState
import io.pilgrimdb.common.model.ProjectState
import kotlin.reflect.full.isSubclassOf
import org.jetbrains.exposed.sql.AutoIncColumnType
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.IntegerColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.VarCharColumnType
import org.reflections.Reflections

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

            val packageName = table.java.`package`.name

            val instance = table.objectInstance as Table
            val fields = instance.columns.map { getField(it) }.toList()

            val modelState = ModelState(instance.tableName, fields.toMutableList())

            state.addModel(packageName, modelState)
        }
        return state
    }

    private fun getField(column: Column<*>): Field {
        return when (column.columnType) {
            is AutoIncColumnType -> AutoField(column.name, primaryKey = true, index = false, unique = false, nullable = false)
            is IntegerColumnType -> IntegerField(column.name, primaryKey = true, index = false, unique = false, nullable = false)
            is VarCharColumnType -> CharField(
                column.name, primaryKey = false, index = false, unique = false, nullable = false,
                maxLength = (column.columnType as VarCharColumnType).colLength
            )
            else -> throw IllegalArgumentException("Not Supported")
        }
    }
}