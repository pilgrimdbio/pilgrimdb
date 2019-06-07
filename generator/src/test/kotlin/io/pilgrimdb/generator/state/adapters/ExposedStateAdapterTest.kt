package io.pilgrimdb.generator.state.adapters

import io.pilgrimdb.common.model.AutoField
import io.pilgrimdb.common.model.CharField
import io.pilgrimdb.common.model.IntegerField
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.jetbrains.exposed.sql.Table
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExposedStateAdapterTest {

    object Test1 : Table() {
        val id = integer("id").primaryKey().autoIncrement()
    }

    @Test
    fun `isSupported, should be true when its a exposed model`() {
        ExposedStateAdapter.isSupported(Test1::class) shouldEqual true
    }

    @Test
    fun `isSupported, should be false when its not a exposed model`() {
        ExposedStateAdapter.isSupported(ExposedStateAdapterTest::class) shouldEqual false
    }
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IntegerColumnTest {

    object Test1 : Table() {
        val id = integer("id").autoIncrement().primaryKey()
    }

    object Test2 : Table() {
        val id = integer("id").autoIncrement()
    }

    object Test3 : Table() {
        val id = integer("test").nullable()
    }

    @Test
    fun `extract autoincrement field`() {
        val result = ExposedStateAdapter.convertModel(Test1::class)

        result.fields.size shouldEqual 1
        result.fields[0] shouldBeInstanceOf AutoField::class
        result.fields[0].name shouldEqual "id"
        result.fields[0].primaryKey shouldEqual true
        result.fields[0].nullable shouldEqual false
    }

    @Test
    fun `extract autoincrement field, not primary key`() {
        val result = ExposedStateAdapter.convertModel(Test2::class)

        result.fields.size shouldEqual 1
        result.fields[0] shouldBeInstanceOf AutoField::class
        result.fields[0].name shouldEqual "id"
        result.fields[0].primaryKey shouldEqual false
        result.fields[0].nullable shouldEqual false
    }

    @Test
    fun `extract simple field that is nullable`() {
        val result = ExposedStateAdapter.convertModel(Test3::class)

        result.fields.size shouldEqual 1
        result.fields[0] shouldBeInstanceOf IntegerField::class
        result.fields[0].name shouldEqual "test"
        result.fields[0].primaryKey shouldEqual false
        result.fields[0].nullable shouldEqual true
    }
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VarCharColumnTest {

    object Test1 : Table() {
        val id = varchar("id", 100).primaryKey()
    }

    @Test
    fun `extract autoincrement field`() {
        val result = ExposedStateAdapter.convertModel(Test1::class)

        result.fields.size shouldEqual 1
        result.fields[0] shouldBeInstanceOf CharField::class
        result.fields[0].name shouldEqual "id"
        result.fields[0].primaryKey shouldEqual true
        result.fields[0].nullable shouldEqual false
        (result.fields[0] as CharField).maxLength shouldEqual 100
    }
}