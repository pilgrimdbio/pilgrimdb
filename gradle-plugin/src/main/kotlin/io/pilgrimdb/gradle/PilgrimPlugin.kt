package io.pilgrimdb.gradle

import io.pilgrimdb.gradle.tasks.MakeMigrationsTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class PilgrimPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val pilgrimExtension = project.extensions.create("pilgrim", PilgrimExtension::class.java)

        project.tasks.create(MakeMigrationsTask.TASK_NAME) {
            it.doLast {
                MakeMigrationsTask(project, pilgrimExtension).generateMigrations()
            }
        }
    }
}