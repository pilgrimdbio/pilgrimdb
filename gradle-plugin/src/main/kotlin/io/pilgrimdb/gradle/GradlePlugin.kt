package io.pilgrimdb.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class GradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.create(MakeMigrationsTask.TASK_NAME, MakeMigrationsTask::class.java)
    }
}