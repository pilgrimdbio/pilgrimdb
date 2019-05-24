package io.pilgrimdb.generator.render.model

import com.squareup.kotlinpoet.CodeBlock
import io.pilgrimdb.common.model.AutoField
import io.pilgrimdb.common.model.CharField
import io.pilgrimdb.common.model.IntegerField
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
    inner class AutoFieldRenderTest {
        @Test
        fun testRender() {
            val field = AutoField("test", primaryKey = true, index = true, unique = true, nullable = true)
            field.render(builder)

            val output = builder.build().toString()

            output shouldEqual """
        |autoField("test") {
        |    primaryKey = true
        |    nullable = true
        |    unique = true
        |    index = true
        |}
        |
        """.trimMargin()
        }
    }

    @Nested
    inner class IntegerFieldRenderTest {
        @Test
        fun testRender() {
            val field = IntegerField("test", primaryKey = true, index = true, unique = true, nullable = true)
            field.render(builder)

            val output = builder.build().toString()

            output shouldEqual """
        |integerField("test") {
        |    primaryKey = true
        |    nullable = true
        |    unique = true
        |    index = true
        |}
        |
        """.trimMargin()
        }
    }

    @Nested
    inner class CharFieldRenderTest {

        @Test
        fun testRender() {
            val field =
                CharField("test", primaryKey = true, index = true, unique = true, nullable = true, maxLength = 100)
            field.render(builder)

            val output = builder.build().toString()

            output shouldEqual """
        |charField("test") {
        |    maxLength = 100
        |    primaryKey = true
        |    nullable = true
        |    unique = true
        |    index = true
        |}
        |
        """.trimMargin()
        }
    }
}