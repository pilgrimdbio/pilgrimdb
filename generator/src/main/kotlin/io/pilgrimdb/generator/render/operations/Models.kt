package io.pilgrimdb.generator.render.operations

import com.squareup.kotlinpoet.CodeBlock
import io.pilgrimdb.common.operations.CreateModel
import io.pilgrimdb.generator.render.model.render

fun CreateModel.render(builder: CodeBlock.Builder) {
    builder.beginControlFlow("createModel(\"$name\")")
    for (field in fields) {
        field.render(builder)
    }
    builder.endControlFlow()
}