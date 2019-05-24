package io.pilgrimdb.generator

import io.pilgrimdb.common.migrations.builders.migration
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MigrationRendererTest {

    @Test
    fun testGenerate() {
        val a = migration("packge", "name") {
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

        val output = MigrationRenderer(a).render()
    }
}