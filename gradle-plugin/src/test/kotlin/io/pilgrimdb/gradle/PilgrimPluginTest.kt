package io.pilgrimdb.gradle

import org.amshove.kluent.shouldNotBeNull
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

class PilgrimPluginTest {

    @Test
    fun testRegistration() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply(PilgrimPlugin::class.java)

        project.tasks.getByName("makemigrations").shouldNotBeNull()
    }
}