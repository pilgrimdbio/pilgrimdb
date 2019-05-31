package io.pilgrimdb.gradle.tasks

import io.pilgrimdb.gradle.PilgrimExtension
import java.io.FileNotFoundException
import javax.inject.Inject
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.TaskAction

open class MakeMigrationsTask() : JavaExec() {

    lateinit var pilgrimExtension: PilgrimExtension

    @Inject
    constructor(pilgrimExtension: PilgrimExtension) : this() {
        this.pilgrimExtension = pilgrimExtension
    }

    companion object {
        const val TASK_NAME = "makemigrations"
    }

    @TaskAction
    override fun exec() {
        setMain("io.pilgrimdb.generator.MainKt")

        val kotlinPath =
            project.convention.getPlugin(JavaPluginConvention::class.java).sourceSets.getByName("main")
                .allSource.srcDirs.first { it.path.contains("/main/kotlin") }
                ?: throw FileNotFoundException("Kotlin folder not found")

        args(
            "makemigrations",
            "--scan-package=${pilgrimExtension.scanPackage}",
            "--base-path=$kotlinPath",
            "--db-url=${pilgrimExtension.dbUrl}",
            "--db-user=${pilgrimExtension.dbUser}",
            "--db-password=${pilgrimExtension.dbPassword}"
        )

        val mainSourcet = project.convention.getPlugin(JavaPluginConvention::class.java).sourceSets.getByName("main")
        classpath(
            mainSourcet.compileClasspath,
            mainSourcet.runtimeClasspath,
            project.buildscript.configurations.getByName("classpath").asPath
        )
        super.exec()
    }
}