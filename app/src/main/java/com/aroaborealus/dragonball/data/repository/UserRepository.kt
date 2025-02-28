package com.aroaborealus.dragonball.data.repository

import org.jetbrains.annotations.VisibleForTesting
import kotlin.random.Random

class UserRepository {

    companion object {
        private var token = ""

    }

    sealed class LoginResponse {
        data object Success : LoginResponse()
        data class Error(val message: String) : LoginResponse()
    }

    fun login(usuario: String, password: String): LoginResponse {
        return if (Random.nextBoolean()) {
            token = "eyJraWQiOiJwcml2YXRlIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJleHBpcmF0aW9uIjo2NDA5MjIxMTIwMCwiZW1haWwiOiJjZHRsQHBydWVibWFpbC5lcyIsImlkZW50aWZ5IjoiRDIwRTAwQTktODY0NC00MUYyLUE0OUYtN0ZDRUY2MTVFMTQ3In0.wMqJfh5qcs5tU6hu2VxT4OV9Svd7BGBA7HsVpKhx5-8"
            LoginResponse.Success
        } else {
            LoginResponse.Error("501")
        }
    }

    fun getToken(): String = token

    @VisibleForTesting
    fun setToken(token: String) { UserRepository.token = token }
}