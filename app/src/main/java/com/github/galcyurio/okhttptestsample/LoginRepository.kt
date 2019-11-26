package com.github.galcyurio.okhttptestsample

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository(private val apiInterface: ApiInterface) : ILoginRepository {

    companion object {

        val TAG = LoginRepository::class.java.simpleName
        private var loginRepository: LoginRepository? = null
        private val apiInterface: ApiInterface =
            RetrofitService.createService(ApiInterface::class.java)

        val instance: LoginRepository
            get() {
                if (loginRepository == null) {
                    loginRepository = LoginRepository(apiInterface)
                }
                return loginRepository as LoginRepository
            }
    }

    override fun getUserLogin(loginFields: LoginFields): LiveData<LoginResponse> {

        val loginResponse = MutableLiveData<LoginResponse>()

        apiInterface.login(loginFields).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    loginResponse.postValue(response.body())
                } else {
                    loginResponse.value = null
                    Log.e(TAG, response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginResponse.value = null
                Log.e(TAG, " Failure getting the logged in user")
                t.printStackTrace()
            }
        })
        return loginResponse
    }
}