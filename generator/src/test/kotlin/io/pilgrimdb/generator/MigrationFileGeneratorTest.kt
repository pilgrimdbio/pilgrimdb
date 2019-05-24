package io.pilgrimdb.generator

import io.pilgrimdb.common.builders.migration
import org.junit.jupiter.api.Test

class MigrationFileGeneratorTest {

    @Test
    fun testGenerate() {
        val a = migration {
            createModel("test1") {
                autoField("id")
                charField("name") {
                    maxLength = 50
                }
            }
            createModel("test2") {
                autoField("id")
                charField("name") {
                    maxLength = 50
                }
                integerField("test4") {
                    index = true
                    primaryKey = true
                }
            }
        }

        val output = MigrationFileGenerator("test.package", a).render()
    }
}