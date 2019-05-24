package io.pilgrimdb.generator

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import io.pilgrimdb.common.Migration
import io.pilgrimdb.generator.extensions.operations.render

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
        builder.addImport("net.buluba.pilgrim.operations", "createModel")
        builder.addImport("net.buluba.pilgrim.operations", "addField")
        // Model
        builder.addImport("net.buluba.pilgrim.model", "autoField")
        builder.addImport("net.buluba.pilgrim.model", "charField")
        builder.addImport("net.buluba.pilgrim.model", "integerField")
    }
}