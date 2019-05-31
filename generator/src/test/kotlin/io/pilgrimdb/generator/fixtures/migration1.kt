package io.pilgrimdb.generator.fixtures

import io.pilgrimdb.common.migrations.builders.migration

var migration1 = migration("io.pilgrimdb.generator.fixtures", "name") {
    createModel("test") {
        autoField("id")
    }
}