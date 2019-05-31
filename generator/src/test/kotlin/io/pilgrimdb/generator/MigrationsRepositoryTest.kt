package io.pilgrimdb.generator

import io.pilgrimdb.common.migrations.operations.Migration
import io.pilgrimdb.generator.fixtures.migration1
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import kotlinx.serialization.json.Json
import kotlinx.serialization.set
import org.amshove.kluent.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MigrationsRepositoryTest {

    val packageName = "io.pilgrimdb.generator.fixtures"

    var tempDir: File? = null
    var kotlinDir: File? = null
    var packageDir: File? = null
    var resourcesDir: File? = null

    @BeforeEach
    fun init() {
        tempDir = Files.createTempDirectory("migrationsRepositoryTest").toFile()
        kotlinDir = File(tempDir, "kotlin")
        kotlinDir!!.mkdirs()
        packageDir = File(kotlinDir, "io/pilgrimdb/generator/fixtures")
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
            MigrationsRepository(kotlinDir!!.absolutePath)
        }

        @Test
        fun testBasePathNotExists() {
            invoking {
                MigrationsRepository("doesntexist")
            } shouldThrow FileNotFoundException::class
        }
    }

    @Nested
    inner class AddMigrationsTest {

        @Test
        fun `migrations folder should be created`() {
            val repository = MigrationsRepository(kotlinDir!!.absolutePath)

            File(packageDir, "migrations").exists().shouldBeFalse()

            repository.addMigration(Migration(packageName, "name"))

            File(packageDir, "migrations").exists().shouldBeTrue()
        }

        @Test
        fun `migration file should be created`() {
            val repository = MigrationsRepository(kotlinDir!!.absolutePath)

            repository.addMigration(Migration(packageName, "name"))

            File(packageDir, "migrations/name.kt").exists().shouldBeTrue()
        }

        @Test
        fun `migration index should be created`() {
            val repository = MigrationsRepository(kotlinDir!!.absolutePath)

            repository.addMigration(Migration(packageName, "name"))

            val index = File(resourcesDir, "pilgrim_index.json")

            index.exists().shouldBeTrue()
            val content = Json.parse(MigrationEntry.serializer().set, index.bufferedReader().use { it.readText() }).toMutableSet()
            content.size shouldEqual 1
            content shouldContain MigrationEntry(packageName, "name")
        }
    }

    @Nested
    inner class GetMigrationsTest {

        @Test
        fun `get all migration should not return instances if index file doesnt exist`() {
            val repository = MigrationsRepository(kotlinDir!!.absolutePath)

            val output = repository.getAllMigrations()
            output.size shouldEqual 0
        }

        @Test
        fun `get all migration should throw exception if resources doesnt exist`() {
            val repository = MigrationsRepository(kotlinDir!!.absolutePath)
            resourcesDir!!.delete()
            invoking { repository.getAllMigrations() } shouldThrow FileNotFoundException::class
        }

        @Test
        fun `get all migrations from index file should return instances`() {
            val repository = MigrationsRepository(kotlinDir!!.absolutePath)

            repository.addMigration(Migration("io.pilgrimdb.generator.fixtures", "Migration1"))

            val output = repository.getAllMigrations()
            output.size shouldEqual 1
            output shouldContain migration1
        }
    }
}