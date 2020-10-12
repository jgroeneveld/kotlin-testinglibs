package de.jgroeneveld.testinglibs

class SomeHandler(val someService: SomeService) {
    fun doHandling(): Response {
        val answer = someService.doServicing()

        return Response(200, "{\"foo_bar\" : \"root!\", \"lorem_ipsum\" : \"$answer\"}", mapOf("Content-Type" to "application/json"))
    }
}

data class Response(val status: Int, val body: String, val headers: Map<String, String>) {
    fun header(key: String): String? {
        return headers[key]
    }
}