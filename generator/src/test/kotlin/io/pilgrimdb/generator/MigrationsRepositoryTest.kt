package io.pilgrimdb.generator

import io.pilgrimdb.common.migrations.operations.Migration
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MigrationsRepositoryTest {

    val packageName = "io.test"

    var tempDir: File? = null
    var packageDir: File? = null

    @BeforeEach
    fun init() {
        tempDir = Files.createTempDirectory("migrationsRepositoryTest").toFile()
        packageDir = File(tempDir, "io/test")
        packageDir!!.mkdirs()
    }

    @AfterEach
    fun clean() {
        tempDir?.deleteRecursively()
    }

    @Nested
    inner class InitializationTest {

        @Test
        fun testNormal() {
            MigrationsRepository(tempDir!!.absolutePath)
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
        fun testMigrationFolderCreation() {
            val repository = MigrationsRepository(tempDir!!.absolutePath)

            File(packageDir, "migrations").exists().shouldBeFalse()

            repository.addMigration(Migration("io.test", "name"))

            File(packageDir, "migrations").exists().shouldBeTrue()
        }

        @Test
        fun testMigrationAdded() {
            val repository = MigrationsRepository(tempDir!!.absolutePath)

            repository.addMigration(Migration("io.test", "name"))

            File(packageDir, "migrations/name.kt").exists().shouldBeTrue()
        }

        // @Test
        // fun testGetAllMigrationNames() {
        //     val repository = MigrationsRepository(tempDir!!.absolutePath)
        //     val migrations = File(packageDir, "migrations")
        //     migrations.mkdir()
        //
        //     val file = File(migrations, "test.kt")
        //
        //     file.createNewFile().shouldBeTrue()
        //
        //     val output = repository.getMigrationNames("io.test")
        //
        //     output shouldContain "test.kt"
        // }
    }
}