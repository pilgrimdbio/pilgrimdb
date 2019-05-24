package io.pilgrimdb.generator.render.operations

import com.squareup.kotlinpoet.CodeBlock
import io.pilgrimdb.generator.render.model.render
import io.pilgrimdb.common.operations.CreateModel

fun CreateModel.render(builder: CodeBlock.Builder) {
    builder.beginControlFlow("createModel(\"$name\")")
    for (field in fields) {
        field.render(builder)
    }
    builder.endControlFlow()
}