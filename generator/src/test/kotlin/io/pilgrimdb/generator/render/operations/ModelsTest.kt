package io.pilgrimdb.generator.render.operations

import com.squareup.kotlinpoet.CodeBlock
import io.pilgrimdb.common.model.AutoField
import io.pilgrimdb.common.operations.CreateModel
import io.pilgrimdb.generator.render.operations.render
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FieldsTest {

    private var builder = CodeBlock.Builder()

    @BeforeEach
    fun init() {
        builder = CodeBlock.Builder()
    }

    @Nested
    inner class CreateModelRenderTest {
        @Test
        fun testRender() {
            val operation = CreateModel(
                "test", fields = mutableListOf(
                    AutoField("id", primaryKey = true, index = false, unique = false, nullable = false)
                )
            )

            operation.render(builder)

            val output = builder.build().toString()
            output shouldEqual """
        |createModel("test") {
        |    autoField("id") {
        |        primaryKey = true
        |    }
        |}
        |
        """.trimMargin()
        }
    }
}