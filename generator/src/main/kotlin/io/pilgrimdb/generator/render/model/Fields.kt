package io.pilgrimdb.generator.render.model

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

fun commonAttributesRender(field: Field, builder: CodeBlock.Builder) {
    if (field.primaryKey) {
        builder.addStatement("primaryKey = true")
    }

    if (field.nullable) {
        builder.addStatement("nullable = true")
    }

    if (field.unique) {
        builder.addStatement("unique = true")
    }

    if (field.index) {
        builder.addStatement("index = true")
    }
}

fun AutoField.render(builder: CodeBlock.Builder) {
    builder.beginControlFlow("autoField(\"$name\")")
    commonAttributesRender(this, builder)
    builder.endControlFlow()
}

fun IntegerField.render(builder: CodeBlock.Builder) {
    builder.beginControlFlow("integerField(\"$name\")")
    commonAttributesRender(this, builder)
    builder.endControlFlow()
}

fun CharField.render(builder: CodeBlock.Builder) {
    builder.beginControlFlow("charField(\"$name\")")
    builder.addStatement("maxLength = $maxLength")
    commonAttributesRender(this, builder)
    builder.endControlFlow()
}
