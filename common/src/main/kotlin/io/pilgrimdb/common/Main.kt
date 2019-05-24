package io.pilgrimdb.common

import io.pilgrimdb.common.builders.migration
import io.pilgrimdb.common.model.ProjectState

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
        charField("name2") {
            maxLength = 100
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
