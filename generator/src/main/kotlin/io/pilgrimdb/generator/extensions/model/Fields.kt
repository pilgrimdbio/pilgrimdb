package io.pilgrimdb.generator.extensions.model

import com.squareup.kotlinpoet.CodeBlock
import io.pilgrimdb.common.model.AutoField
import io.pilgrimdb.common.model.CharField
import io.pilgrimdb.common.model.Field
import io.pilgrimdb.common.model.IntegerField

fun Field.render(builder: CodeBlock.Builder) {
    when (this) {
        is AutoField -> this.render(builder)
        is IntegerField -> this.render(builder)
        is CharField -> this.render(builder)
        else -> throw NotImplementedError("Field ${this::class::simpleName} is not implemented")
    }
}

fun AutoField.render(builder: CodeBlock.Builder) {
    builder.beginControlFlow("autoField($name)")
    builder.endControlFlow()
}

fun IntegerField.render(builder: CodeBlock.Builder) {
    builder.beginControlFlow("integerField($name)")
    if (unique) {
        builder.addStatement("unique = true")
    }
    builder.endControlFlow()
}

fun CharField.render(builder: CodeBlock.Builder) {
    builder.beginControlFlow("charField($name)")
    builder.addStatement("maxLength = $maxLength")
    builder.endControlFlow()
}
