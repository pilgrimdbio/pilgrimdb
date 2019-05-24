package io.pilgrimdb.common.migrations

import io.pilgrimdb.common.migrations.operations.Migration

object MigrationsRegistry {

    /**
     * A collection of all migrations, indexed by their package
     */
    private val migrations: MutableMap<String, MutableList<Migration>> = mutableMapOf()

    fun registerMigration(packageName: String, migration: Migration) {

        val packageMigrations = migrations.getOrPut(packageName) { mutableListOf() }
        packageMigrations.add(migration)
    }

    fun getAllMigrations(): MutableMap<String, MutableList<Migration>> = migrations
}