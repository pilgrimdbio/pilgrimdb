package io.pilgrimdb.generator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import io.pilgrimdb.common.Migration
import io.pilgrimdb.generator.render.operations.render

class MigrationFileGenerator(private val packageName: String, private val migration: Migration) {

    fun render(): String {
        val content = CodeBlock.builder()
        migration.render(content)
        val file = FileSpec.builder(packageName, "migration.kt")
        addImports(file)
        file.addProperty(PropertySpec.builder("migration", Migration::class).initializer(content.build()).build())

        return file.build().toString()
    }

    private fun addImports(builder: FileSpec.Builder) {
        // Operations
        builder.addImport("net.buluba.pilgrim.builders", "createModel")
        builder.addImport("net.buluba.pilgrim.builders", "addField")
        // Model
        builder.addImport("net.buluba.pilgrim.builders", "autoField")
        builder.addImport("net.buluba.pilgrim.builders", "charField")
        builder.addImport("net.buluba.pilgrim.builders", "integerField")
    }
}