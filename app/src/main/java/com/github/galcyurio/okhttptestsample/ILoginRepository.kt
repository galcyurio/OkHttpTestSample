package com.github.galcyurio.okhttptestsample

import androidx.lifecycle.LiveData

interface ILoginRepository {
    fun getUserLogin(loginFields: LoginFields): LiveData<LoginResponse>
}