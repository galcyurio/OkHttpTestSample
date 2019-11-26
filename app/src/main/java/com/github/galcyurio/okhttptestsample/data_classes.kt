package com.github.galcyurio.okhttptestsample

data class LoginResponse(
    val foo: String,
    val bar: String,
    val user: User
)

data class User(
    val id: Long,
    val name: String
)

data class LoginFields(
    val email: String,
    val password: String
)