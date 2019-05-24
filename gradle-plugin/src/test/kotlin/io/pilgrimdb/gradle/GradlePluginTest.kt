package io.pilgrimdb.gradle

import org.amshove.kluent.shouldNotBeNull
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

class GradlePluginTest {

    @Test
    fun oneTest() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(GradlePlugin::class.java)

        project.tasks.getByName("makemigrations").shouldNotBeNull()
    }
}