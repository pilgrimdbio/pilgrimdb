package io.pilgrimdb.gradle

import assertk.assertThat
import assertk.assertions.isNotNull
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Ignore
import org.junit.Test

class GradlePluginTest {

    @Ignore
    @Test
    fun oneTest() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(GradlePlugin::class.java)

        assertThat(project.tasks.getByName("makemigrations")).isNotNull()
    }
}