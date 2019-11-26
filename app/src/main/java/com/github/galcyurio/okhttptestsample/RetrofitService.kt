package com.github.galcyurio.okhttptestsample

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * @author galcyurio
 */
object RetrofitService {
    fun <T> createService(
        clazz: Class<T>,
        baseUrl: String = "https://example.com"
    ): T {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
            .create(clazz)
    }
}