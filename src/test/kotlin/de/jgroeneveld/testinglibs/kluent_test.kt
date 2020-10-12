package de.jgroeneveld.testinglibs

import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldEqual
import kotlin.test.Test
import kotlin.test.assertEquals

class kluent_test {
    val someService = mockk<SomeService>()

    val handler = SomeHandler(someService)

    @Test
    fun testSomeHandler() {
        every { someService.doServicing() } returns "Mock Answer"

        val response = handler.doHandling()

        // raw
        response.status shouldEqual 200
        response.body shouldEqual "{\"foo_bar\" : \"root!\", \"lorem_ipsum\" : \"Mock Answer\"}"
        response.header("Content-Type") shouldEqual "application/json"

        // custom matchers
        response shouldHaveStatus 200
        response shouldHaveBody "{\"foo_bar\" : \"root!\", \"lorem_ipsum\" : \"Mock Answer\"}"
        response shouldHaveHeader ("Content-Type" to "application/json")
    }

    // ******************************** extensions ********************************
    infix fun Response.shouldHaveStatus(expectedStatus: Int): Response {
        return this.apply { assertEquals(expectedStatus, this.status, "Response:\n$this\n\nStatus") }
    }

    infix fun Response.shouldHaveBody(body: String): Response {
        return this.apply { assertEquals(body, this.body, "Response:\n$this\n\nBody") }
    }

    infix fun Response.shouldHaveHeader(header: Pair<String, String>): Response {
        return this.apply {
            assertEquals(
                header.second,
                this.header(header.first),
                "Response:\n$this\n\nHeader '${header.first}'"
            )
        }
    }
}
