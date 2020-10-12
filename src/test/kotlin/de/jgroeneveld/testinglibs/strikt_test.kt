package de.jgroeneveld.testinglibs

import io.mockk.every
import io.mockk.mockk
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.test.Test

class strikt_test {
    val someService = mockk<SomeService>()

    val handler = SomeHandler(someService)

    @Test
    fun testSomeHandler() {
        every { someService.doServicing() } returns "Mock Answer"

        val response = handler.doHandling()

        // raw
        expectThat(response.status).isEqualTo(200)
        expectThat(response.body).isEqualTo("{\"foo_bar\" : \"root!\", \"lorem_ipsum\" : \"Mock Answer\"}")
        expectThat(response.header("Content-Type")).isEqualTo("application/json")

        // raw with custom mappings
        expectThat(response).status.isEqualTo(200)
        expectThat(response).body.isEqualTo("{\"foo_bar\" : \"root!\", \"lorem_ipsum\" : \"Mock Answer\"}")
        expectThat(response).header("Content-Type").isEqualTo("application/json")

        // block assertions
        expectThat(response) {
            status.isEqualTo(200)
            body.isEqualTo("{\"foo_bar\" : \"root!\", \"lorem_ipsum\" : \"Mock Answer\"}")
            header("Content-Type").isEqualTo("application/json")
        }

        // custom matchers
        expectThat(response).hasStatus(200)
        expectThat(response).hasBody("{\"foo_bar\" : \"root!\", \"lorem_ipsum\" : \"Mock Answer\"}")
        expectThat(response).hasHeader("Content-Type", "application/json")

        // custom matchers with block assertions
        expectThat(response) {
            hasStatus(200)
            hasBody("{\"foo_bar\" : \"root!\", \"lorem_ipsum\" : \"Mock Answer\"}")
            hasHeader("Content-Type", "application/json")
        }
    }

    // ******************************** extensions ********************************
    fun Assertion.Builder<Response>.hasStatus(status: Int) = assert("has status %s", status) {
        when (it.status) {
            status -> pass()
            else -> fail(actual = it.status)
        }
    }

    fun Assertion.Builder<Response>.hasBody(body: String) = assert("has body %s", body) {
        when (it.body) {
            body -> pass()
            else -> fail(actual = it.body)
        }
    }

    fun Assertion.Builder<Response>.hasHeader(name: String, value: String) = assert("has header %s", name) {
        val actual = it.header(name)
        when (actual) {
            value -> pass()
            else -> fail(actual = actual)
        }
    }

    // ******************************** custom mappings ********************************
    val Assertion.Builder<Response>.status: Assertion.Builder<Int>
        get() = get { status }

    val Assertion.Builder<Response>.body: Assertion.Builder<String>
        get() = get { body }

    fun Assertion.Builder<Response>.header(name: String): Assertion.Builder<String?> {
        return get { header(name) }
    }
}