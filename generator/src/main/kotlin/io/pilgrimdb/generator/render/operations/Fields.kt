package io.pilgrimdb.generator.render.operations

import com.squareup.kotlinpoet.CodeBlock
import io.pilgrimdb.generator.render.model.render
import io.pilgrimdb.common.operations.AddField

fun AddField.render(builder: CodeBlock.Builder) {
    builder.beginControlFlow("addField(\"$tableName\")")
    field.render(builder)
    builder.endControlFlow()
}