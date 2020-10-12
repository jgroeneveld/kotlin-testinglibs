package de.jgroeneveld.testinglibs

import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import ch.tutteli.atrium.creating.Expect
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test

class atrium_test {
    val someService = mockk<SomeService>()

    val handler = SomeHandler(someService)

    @Test
    fun testSomeHandler() {
        every { someService.doServicing() } returns "Mock Answer"

        val response = handler.doHandling()

        // raw
        expect(response.status).toBe(200)
        expect(response.body).toBe("{\"foo_bar\" : \"root!\", \"lorem_ipsum\" : \"Mock Answer\"}")
        expect(response.header("Content-Type")).toBe("application/json")

        // custom matchers
        expect(response).hasStatusOK()
        expect(response).hasBody("{\"foo_bar\" : \"root!\", \"lorem_ipsum\" : \"Mock Answer\"}")
        expect(response).hasHeader("Content-Type", "application/json")

        // custom matchers and block body to run all of them together
        expect(response) {
            hasStatusOK()
            hasBody("{\"foo_bar\" : \"root!\", \"lorem_ipsum\" : \"Mock Answer\"}")
            hasHeader("Content-Type", "application/json")
        }
    }

    // ******************************** extensions ********************************
    fun Expect<Response>.hasStatusOK(): Expect<Response> {
        return createAndAddAssertion("has status", 200) {
            it.status == 200
        }
    }

    fun Expect<Response>.hasBody(expectedBody: String): Expect<Response> {
        return createAndAddAssertion("has body", expectedBody) {
            it.body == expectedBody
        }
    }

    fun Expect<Response>.hasHeader(key: String, value: String): Expect<Response> {
        return createAndAddAssertion("has header", "$key: $value") {
            it.header(key) == value
        }
    }
}
