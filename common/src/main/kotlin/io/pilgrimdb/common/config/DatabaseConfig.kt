package io.pilgrimdb.common.config

data class DatabaseConfig(val username: String, val password: String, val url: String, val schema: String? = null)