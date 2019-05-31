package io.pilgrimdb.generator

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import io.pilgrimdb.generator.exposed.ExposedStateProvider
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class PilgrimDB : CliktCommand() {
    override fun run() = Unit
}

class MakeMigrations : CliktCommand(name = "makemigrations") {
    val basePath: String by option(help = "Path to the base source folder (src/kotlin)").required()
    val scanPackage: String by option(help = "The package name to scan for entities").required()
    val dbUrl: String by option(help = "Jdbc url of the database").required()
    val dbUser: String by option(help = "User for the database connection").required()
    val dbPassword: String by option(help = "Password for the database connection").required()

    override fun run() {
        logger.debug { "makemigrations: scanPackage: $scanPackage, dbUrl: $dbUrl, dbUser: $dbUser" }

        val generator = MigrationGenerator(basePath, ExposedStateProvider(scanPackage))
        generator.makeMigrations()
    }
}

fun main(args: Array<String>) = PilgrimDB().subcommands(MakeMigrations()).main(args)
