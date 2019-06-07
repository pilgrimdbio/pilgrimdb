package io.pilgrimdb.common.test.fixtures.migrations

import io.pilgrimdb.common.migrations.builders.migration

var migration1 = migration("io.pilgrimdb.generator.fixtures", "name") {
    dependsOn("test", "test")
    createModel("test") {
        autoField("id")
    }
}