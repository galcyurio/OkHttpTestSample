package com.github.galcyurio.okhttptestsample

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author galcyurio
 */
class LoginRepository2(
    private val apiInterface: ApiInterface
) {
    // Use something that doesn't depend on Android
    // such as kotlinx.coroutines, ReactiveX and callback...
    // In this example uses callback.

    fun login(
        loginFields: LoginFields,
        onSuccess: (LoginResponse) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        return apiInterface.login(loginFields).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    onSuccess(response.body()!!)
                } else {
                    onFailure(RuntimeException(response.errorBody()?.string()))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}