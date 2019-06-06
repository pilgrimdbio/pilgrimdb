package io.pilgrimdb.common.migrations

import io.pilgrimdb.common.migrations.operations.Migration
import io.pilgrimdb.common.migrations.operations.MigrationIndex
import io.pilgrimdb.common.test.fixtures.migration1
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import kotlinx.serialization.json.Json
import kotlinx.serialization.set
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RepositoryTest {

    val packageName = "io.pilgrimdb.common.test.fixtures"

    var tempDir: File? = null
    var kotlinDir: File? = null
    var packageDir: File? = null
    var resourcesDir: File? = null

    @BeforeEach
    fun init() {
        tempDir = Files.createTempDirectory("migrationsRepositoryTest").toFile()
        kotlinDir = File(tempDir, "kotlin")
        kotlinDir!!.mkdirs()
        packageDir = File(kotlinDir, "io/pilgrimdb/common/test/fixtures")
        packageDir!!.mkdirs()
        resourcesDir = File(tempDir, "resources")
        resourcesDir!!.mkdir()
    }

    @AfterEach
    fun clean() {
        tempDir?.deleteRecursively()
    }

    @Nested
    inner class InitializationTest {

        @Test
        fun testNormal() {
            Repository(kotlinDir!!.absolutePath)
        }

        @Test
        fun testBasePathNotExists() {
            invoking {
                Repository("doesntexist")
            } shouldThrow FileNotFoundException::class
        }
    }

    @Nested
    inner class AddMigrationsTest {

        @Test
        fun `migrations folder should be created`() {
            val repository = Repository(kotlinDir!!.absolutePath)

            File(packageDir, "migrations").exists().shouldBeFalse()

            repository.addMigration(Migration(packageName, "name"), "test")

            File(packageDir, "migrations").exists().shouldBeTrue()
        }

        @Test
        fun `migration file should be created`() {
            val repository = Repository(kotlinDir!!.absolutePath)

            repository.addMigration(Migration(packageName, "name"), "test")

            File(packageDir, "migrations/name.kt").exists().shouldBeTrue()
        }

        @Test
        fun `migration index should be created`() {
            val repository = Repository(kotlinDir!!.absolutePath)

            repository.addMigration(Migration(packageName, "name"), "test")

            val index = File(resourcesDir, "pilgrim_index.json")

            index.exists().shouldBeTrue()
            val content =
                Json.parse(MigrationIndex.serializer().set, index.bufferedReader().use { it.readText() }).toMutableSet()
            content.size shouldEqual 1
            content shouldContain MigrationIndex(packageName, "name")
        }
    }

    @Nested
    inner class GetMigrationsTest {

        @Test
        fun `get all migration should not return instances if index file doesnt exist`() {
            val repository = Repository(kotlinDir!!.absolutePath)

            val output = repository.getAllMigrations()
            output.size shouldEqual 0
        }

        @Test
        fun `get all migration should throw exception if resources doesnt exist`() {
            val repository = Repository(kotlinDir!!.absolutePath)
            resourcesDir!!.delete()
            invoking { repository.getAllMigrations() } shouldThrow FileNotFoundException::class
        }

        @Test
        fun `get all migrations from index file should return instances`() {
            val repository = Repository(kotlinDir!!.absolutePath)

            repository.addMigration(Migration(packageName, "Migration1"), "test")

            val output = repository.getAllMigrations()
            output.size shouldEqual 1
            output shouldContain migration1
        }
    }
}