package com.github.galcyurio.okhttptestsample

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    /*
    Login to the api
     */
    @POST("api/auth/login")
    fun login(@Body loginFields: LoginFields): Call<LoginResponse>
}