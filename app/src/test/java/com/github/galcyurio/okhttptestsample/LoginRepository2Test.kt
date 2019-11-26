package com.github.galcyurio.okhttptestsample

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * @author galcyurio
 */
class LoginRepository2Test {
    private lateinit var server: MockWebServer
    private lateinit var repository: LoginRepository2

    @Before
    fun setUp() {
        server = MockWebServer()

        val apiInterface = RetrofitService.createService(
            clazz = ApiInterface::class.java,
            baseUrl = server.url("").toString()
        )
        repository = LoginRepository2(apiInterface)
    }

    // No need Robolectric
    // No need androidx core-testing
    // because LoginRepository2 not depends on Android.
    // Compare the speed of testing.

    @Test
    fun `test something`() {
        // given
        val json = File("src/test/resources/mock-response.json").readText()
        server.enqueue(MockResponse().setBody(json))
        val loginFields = LoginFields("test@test.com", "test@123")

        // when
        val latch = CountDownLatch(1)
        var actual: LoginResponse? = null
        repository.login(
            loginFields = loginFields,
            onSuccess = { actual = it; latch.countDown() },
            onFailure = { throw it }
        )
        latch.await(1, TimeUnit.SECONDS)

        // then
        assertThat(actual?.user?.id, `is`(35L))
        assertThat(actual?.user?.name, `is`("Prashanna Bhandary"))
    }
}