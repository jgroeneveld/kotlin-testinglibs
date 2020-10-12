package de.jgroeneveld.testinglibs

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test

// assertk https://github.com/willowtreeapps/assertk
class assertk_test {
    val someService = mockk<SomeService>()

    val handler = SomeHandler(someService)

    @Test
    fun testSomeHandler() {
        every { someService.doServicing() } returns "Mock Answer"

        val response = handler.doHandling()

        // raw
        assertThat(response.status).isEqualTo(200)
        assertThat(response.body).isEqualTo("{\"foo_bar\" : \"root!\", \"lorem_ipsum\" : \"Mock Answer\"}")
        assertThat(response.header("Content-Type")).isEqualTo("application/json")

        // custom matchers
        assertThat(response)
            .hasStatusOK()
            .hasBody("{\"foo_bar\" : \"root!\", \"lorem_ipsum\" : \"Mock Answer\"}")
    }

    // ******************************** extensions ********************************
    fun Assert<Response>.hasStatusOK(): Assert<Response> = this.hasStatus(200)

    fun Assert<Response>.hasStatus(status: Int): Assert<Response> {
        prop("status", Response::status).isEqualTo(status)
        return this
    }

    fun Assert<Response>.hasBody(body: String): Assert<Response> {
        prop("body", Response::body).isEqualTo(body)
        return this
    }
}
