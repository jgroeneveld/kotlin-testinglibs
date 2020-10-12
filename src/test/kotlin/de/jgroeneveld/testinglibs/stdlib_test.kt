package de.jgroeneveld.testinglibs

import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class stdlib_test {
    val someService = mockk<SomeService>()

    val handler = SomeHandler(someService)

    @Test
    fun testSomeHandler() {
        every { someService.doServicing() } returns "Mock Answer"

        val response = handler.doHandling()

        // raw
        assertEquals(200, response.status)
        assertEquals("{\"foo_bar\" : \"root!\", \"lorem_ipsum\" : \"Mock Answer\"}", response.body)
        assertEquals("application/json", response.header("Content-Type"))

        // custom matchers
        assertResponseStatus(200, response)
        assertResponseBody("{\"foo_bar\" : \"root!\", \"lorem_ipsum\" : \"Mock Answer\"}", response)
        assertResponseHeader("Content-Type", "application/json", response)
    }

    // ******************************** extensions ********************************
    fun assertResponseStatus(expected: Int, response: Response) {
        assertEquals(expected, response.status, "Response:\n$response\n\nStatus")
    }

    fun assertResponseBody(expected: String, response: Response) {
        assertEquals(expected, response.body, "Response:\n$response\n\nBody")
    }

    fun assertResponseHeader(key: String, expectedValue: String, response: Response) {
        assertEquals(expectedValue, response.header(key), "Response:\n$response\n\nHeader '$key'")
    }
}
