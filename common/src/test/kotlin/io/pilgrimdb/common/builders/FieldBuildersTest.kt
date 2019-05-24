package io.pilgrimdb.common.builders

import io.pilgrimdb.common.model.AutoField
import io.pilgrimdb.common.model.CharField
import io.pilgrimdb.common.model.Field
import io.pilgrimdb.common.model.IntegerField
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.properties.Delegates

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FieldBuildersTest {

    @Nested
    inner class AutoFieldTest {

        @Test
        fun testBuild() {
            val builder = object : AutoFieldBuilderDSL {
                var field by Delegates.notNull<Field>()
                override fun addField(field: Field) {
                    this.field = field
                }
            }

            builder.autoField("test") {
                primaryKey = true
                unique = true
                index = true
                nullable = true
            }

            builder.field shouldBeInstanceOf AutoField::class
            val output = builder.field as AutoField

            output.shouldNotBeNull()
            output.name shouldBeEqualTo "test"
            output.primaryKey.shouldBeTrue()
            output.unique.shouldBeTrue()
            output.index.shouldBeTrue()
            output.nullable.shouldBeTrue()
        }
    }

    @Nested
    inner class IntegerFieldTest {

        @Test
        fun testBuild() {
            val builder = object : IntegerFieldBuilderDSL {
                var field by Delegates.notNull<Field>()
                override fun addField(field: Field) {
                    this.field = field
                }
            }

            builder.integerField("test") {
                primaryKey = true
                unique = true
                index = true
                nullable = true
            }

            builder.field shouldBeInstanceOf IntegerField::class
            val output = builder.field as IntegerField

            output.shouldNotBeNull()
            output.name shouldBeEqualTo "test"
            output.primaryKey.shouldBeTrue()
            output.unique.shouldBeTrue()
            output.index.shouldBeTrue()
            output.nullable.shouldBeTrue()
        }
    }

    @Nested
    inner class CharFieldTest {

        @Test
        fun testBuild() {
            val builder = object : CharFieldBuilderDSL {
                var field by Delegates.notNull<Field>()
                override fun addField(field: Field) {
                    this.field = field
                }
            }

            builder.charField("test") {
                maxLength = 150
                primaryKey = true
                unique = true
                index = true
                nullable = true
            }

            builder.field shouldBeInstanceOf CharField::class
            val output = builder.field as CharField

            output.shouldNotBeNull()
            output.name shouldBeEqualTo "test"
            output.maxLength shouldEqualTo 150
            output.primaryKey.shouldBeTrue()
            output.unique.shouldBeTrue()
            output.index.shouldBeTrue()
            output.nullable.shouldBeTrue()
        }
    }
}
