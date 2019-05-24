package io.pilgrimdb.generator.render.operations

import com.squareup.kotlinpoet.CodeBlock
import io.pilgrimdb.common.migrations.operations.AddField
import io.pilgrimdb.common.migrations.operations.CreateModel
import io.pilgrimdb.common.migrations.operations.Operation

fun Operation.render(builder: CodeBlock.Builder) {
    when (this) {
        is CreateModel -> this.render(builder)
        is AddField -> this.render(builder)
        else -> throw NotImplementedError()
    }
}