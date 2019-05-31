package io.pilgrimdb.gradle

import io.pilgrimdb.gradle.tasks.MakeMigrationsTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class PilgrimPlugin : Plugin<Project> {

    var extension: PilgrimExtension? = null

    override fun apply(project: Project) {

        extension = project.extensions.create("pilgrim", PilgrimExtension::class.java)
        // val files = project.configurations.getByName("compileClasspath").files
        val task = project.tasks.create(MakeMigrationsTask.TASK_NAME, MakeMigrationsTask::class.java, extension)
        task.dependsOn.add(project.getTasksByName("classes", true))
    }
}