package io.pilgrimdb.generator.render.operations

import com.squareup.kotlinpoet.CodeBlock
import io.pilgrimdb.common.operations.AddField
import io.pilgrimdb.common.operations.CreateModel
import io.pilgrimdb.common.operations.Operation

fun Operation.render(builder: CodeBlock.Builder) {
    when (this) {
        is CreateModel -> this.render(builder)
        is AddField -> this.render(builder)
        else -> throw NotImplementedError()
    }
}