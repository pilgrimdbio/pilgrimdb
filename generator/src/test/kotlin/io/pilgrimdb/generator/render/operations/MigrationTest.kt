package io.pilgrimdb.generator.render.operations

import com.squareup.kotlinpoet.CodeBlock
import io.pilgrimdb.common.migrations.operations.CreateModel
import io.pilgrimdb.common.migrations.operations.Migration
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MigrationTest {

    private var builder = CodeBlock.Builder()

    @BeforeEach
    fun init() {
        builder = CodeBlock.Builder()
    }

    @Test
    fun testRender() {
        val field = Migration(
            mutableListOf(
                CreateModel("test")
            )
        )

        field.render(builder)

        val output = builder.build().toString()
        output shouldEqual """
        |migration {
        |    createModel("test") {
        |    }
        |}
        |
        """.trimMargin()
    }
}