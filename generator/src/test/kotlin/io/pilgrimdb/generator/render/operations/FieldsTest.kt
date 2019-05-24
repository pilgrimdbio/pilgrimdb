package io.pilgrimdb.generator.render.operations

import com.squareup.kotlinpoet.CodeBlock
import io.pilgrimdb.common.migrations.operations.AddField
import io.pilgrimdb.common.model.AutoField
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FieldsOperationsTest {

    private var builder = CodeBlock.Builder()

    @BeforeEach
    fun init() {
        builder = CodeBlock.Builder()
    }

    @Nested
    inner class AddFieldRenderTest {
        @Test
        fun testRender() {
            val operation = AddField(
                "table",
                AutoField("test", primaryKey = true, index = false, unique = false, nullable = false)
            )

            operation.render(builder)

            val output = builder.build().toString()
            output shouldEqual """
        |addField("table") {
        |    autoField("test") {
        |        primaryKey = true
        |    }
        |}
        |
        """.trimMargin()
        }
    }
}