package io.pilgrimdb.generator.state.adapters

import io.pilgrimdb.common.model.AutoField
import io.pilgrimdb.common.model.CharField
import io.pilgrimdb.common.model.Field
import io.pilgrimdb.common.model.IntegerField
import io.pilgrimdb.common.model.ModelState
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import org.jetbrains.exposed.sql.AutoIncColumnType
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.IntegerColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.VarCharColumnType

object ExposedStateAdapter : StateAdapter {

    override fun isSupported(klass: KClass<out Any>): Boolean = klass.isSubclassOf(Table::class)

    override fun convertModel(klass: KClass<out Any>): ModelState {
        val instance = klass.objectInstance as Table
        val fields = instance.columns.map { getField(it) }.toList()

        return ModelState(instance.tableName, fields.toMutableList())
    }

    private fun getField(column: Column<*>): Field {
        return when (column.columnType) {
            is AutoIncColumnType -> AutoField(
                column.name,
                primaryKey = column.isPrimaryKey(),
                index = false,
                unique = false,
                nullable = column.columnType.nullable
            )
            is IntegerColumnType -> IntegerField(
                column.name,
                primaryKey = column.isPrimaryKey(),
                index = false,
                unique = false,
                nullable = column.columnType.nullable
            )
            is VarCharColumnType -> CharField(
                column.name,
                primaryKey = column.isPrimaryKey(),
                index = false,
                unique = false,
                nullable = column.columnType.nullable,
                maxLength = (column.columnType as VarCharColumnType).colLength
            )
            else -> throw IllegalArgumentException("Not Supported")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun Column<*>.isPrimaryKey(): Boolean {
        val f = this::class.memberProperties.find { it.name == "indexInPK" } as KProperty1<Column<*>, Int?>
        f.isAccessible = true
        return f.get(this) != null
    }
}
