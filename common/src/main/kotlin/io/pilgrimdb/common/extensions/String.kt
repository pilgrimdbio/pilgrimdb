package io.pilgrimdb.common.extensions

import org.apache.commons.text.StringSubstitutor

fun String.substitute(variables: Map<String, String>): String {
    val sub = StringSubstitutor(variables)
    sub.setVariablePrefix("#{")
    sub.setVariableSuffix("}")
    sub.escapeChar = '#'
    return sub.replace(this)
}
