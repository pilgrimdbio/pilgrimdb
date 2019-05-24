package io.pilgrimdb.common.migrations.builders

import io.pilgrimdb.common.migrations.operations.AddField
import io.pilgrimdb.common.migrations.operations.CreateModel
import io.pilgrimdb.common.migrations.operations.Operation
import io.pilgrimdb.common.model.AutoField
import kotlin.properties.Delegates
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OperationBuildersTest {

    @Nested
    inner class CreateModelTest {

        @Test
        fun testBuild() {
            val builder = object : CreateModelBuilderDSL {
                var operation by Delegates.notNull<Operation>()

                override fun addOperation(operation: Operation) {
                    this.operation = operation
                }
            }

            builder.createModel("test") {
                autoField("fieldName")
            }

            builder.operation shouldBeInstanceOf CreateModel::class
            val output = builder.operation as CreateModel

            output.shouldNotBeNull()
            output.name shouldBeEqualTo "test"
            output.fields.size shouldEqualTo 1
            output.fields[0] shouldBeInstanceOf AutoField::class
            output.fields[0].name shouldBeEqualTo "fieldName"
        }
    }

    @Nested
    inner class AddFieldTest {

        @Test
        fun testBuild() {
            val builder = object : AddFieldBuilderDSL {
                var operation by Delegates.notNull<Operation>()

                override fun addOperation(operation: Operation) {
                    this.operation = operation
                }
            }

            builder.addField("test") {
                autoField("fieldName")
            }

            builder.operation shouldBeInstanceOf AddField::class
            val output = builder.operation as AddField

            output.shouldNotBeNull()
            output.tableName shouldBeEqualTo "test"
            output.field shouldBeInstanceOf AutoField::class
            output.field.name shouldBeEqualTo "fieldName"
        }
    }
}