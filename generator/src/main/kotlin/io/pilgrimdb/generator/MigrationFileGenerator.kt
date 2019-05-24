package io.pilgrimdb.generator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import io.pilgrimdb.common.operations.Migration
import io.pilgrimdb.generator.render.operations.render

class MigrationFileGenerator(private val packageName: String, private val migration: Migration) {

    fun render(): String {
        val content = CodeBlock.builder()
        migration.render(content)
        return FileSpec.builder(packageName, "migration.kt")
            .addImport("io.pilgrimdb.common.builders", "migration")
            .addProperty(PropertySpec.builder("migration", Migration::class).initializer(content.build()).build())
            .build()
            .toString()
    }
}