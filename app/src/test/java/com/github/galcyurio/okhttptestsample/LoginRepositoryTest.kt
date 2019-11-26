package com.github.galcyurio.okhttptestsample

import android.os.Looper.getMainLooper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * @author galcyurio
 */
@RunWith(RobolectricTestRunner::class)
class LoginRepositoryTest {
    @get: Rule
    val rule = InstantTaskExecutorRule() // for LiveData

    private lateinit var server: MockWebServer
    private lateinit var repository: LoginRepository

    @Before
    fun setUp() {
        server = MockWebServer()

        // Instantiate ApiInterface like your production code.
        val apiInterface: ApiInterface = RetrofitService.createService(
            clazz = ApiInterface::class.java,
            baseUrl = server.url("").toString()
        )

        // Instantiate LoginRepository using constructor.
        repository = LoginRepository(apiInterface)
    }

    // The first time you test, it's slow because of the download.
    @Config(sdk = [28])
    @Test
    fun `test something`() {
        // given
        val json = File("src/test/resources/mock-response.json").readText()
        server.enqueue(MockResponse().setBody(json))
        val loginFields = LoginFields("test@test.com", "test@123")

        // when
        val actual: LoginResponse = repository.getUserLogin(loginFields).await()

        // then
        assertThat(actual.user.id, Matchers.`is`(35L))
        assertThat(actual.user.name, Matchers.`is`("Prashanna Bhandary"))
    }

    private fun <T> LiveData<T>.await(): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(t: T) {
                data = t
                latch.countDown()
                this@await.removeObserver(this)
            }
        }
        this.observeForever(observer)
        latch.await(1, TimeUnit.SECONDS)
        shadowOf(getMainLooper()).idle() // for LiveData

        @Suppress("UNCHECKED_CAST")
        return data as T
    }
}