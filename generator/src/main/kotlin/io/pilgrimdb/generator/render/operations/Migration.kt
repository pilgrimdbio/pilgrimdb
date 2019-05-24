package io.pilgrimdb.generator.render.operations

import com.squareup.kotlinpoet.CodeBlock
import io.pilgrimdb.common.migrations.operations.Migration

fun Migration.render(builder: CodeBlock.Builder) {
    builder.beginControlFlow("migration")
    for (operation in operations) {
        operation.render(builder)
    }
    builder.endControlFlow()
}