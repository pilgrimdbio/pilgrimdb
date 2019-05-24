package io.pilgrimdb.generator.render.operations

import com.squareup.kotlinpoet.CodeBlock
import io.pilgrimdb.common.operations.AddField
import io.pilgrimdb.generator.render.model.render

fun AddField.render(builder: CodeBlock.Builder) {
    builder.beginControlFlow("addField(\"$tableName\")")
    field.render(builder)
    builder.endControlFlow()
}