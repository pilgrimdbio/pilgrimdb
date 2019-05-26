package io.pilgrimdb.common.db.postgresql

import io.mockk.clearAllMocks
import io.mockk.mockk
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class PostgresOperationsTest {

    val connection = mockk<PostgresConnection>()
    val operations = PostgresOperations(connection)

    @AfterEach
    fun cleanUp() {
        clearAllMocks()
    }

    @Test
    fun `Test quotaName`() {
        val testString = "something"
        operations.quoteName(testString) shouldEqual "\"something\""
    }

    @Test
    fun `Test quotaName when string is already quoted`() {
        val testString = "\"something\""
        operations.quoteName(testString) shouldEqual "\"something\""
    }
}