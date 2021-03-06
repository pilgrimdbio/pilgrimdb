package io.pilgrimdb.generator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import io.pilgrimdb.common.migrations.operations.Migration
import io.pilgrimdb.generator.render.operations.render

class MigrationRenderer(private val migration: Migration) {

    fun render(): String {
        val content = CodeBlock.builder()
        migration.render(content)
        return FileSpec.builder(migration.packageName, "${migration.migrationName}.kt")
            .addImport("io.pilgrimdb.common.migrations.builders", "migration")
            .addProperty(PropertySpec.builder("migration", Migration::class).initializer(content.build()).build())
            .build()
            .toString()
    }
}