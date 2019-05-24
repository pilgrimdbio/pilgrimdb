package io.pilgrimdb.common

import io.pilgrimdb.common.builders.autoField
import io.pilgrimdb.common.builders.charField
import io.pilgrimdb.common.builders.integerField
import io.pilgrimdb.common.model.ProjectState
import io.pilgrimdb.common.operations.addField
import io.pilgrimdb.common.operations.createModel

val a = migration {
    createModel("test1") {
        autoField("id")
        charField("name") {
            maxLength = 50
        }
    }
    createModel("test2") {
        autoField("id")
        charField("name") {
            maxLength = 50
        }
        integerField("test4") {
            index = true
            primaryKey = true
        }
    }
    addField("test1") {
        integerField("Test added") {
            unique = true
        }
    }
}

fun main() {
    val state = ProjectState()
    a.mutateState(state)
    val e = 3
}
