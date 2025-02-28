package com.aroaborealus.dragonball.data.repository


import org.jetbrains.annotations.VisibleForTesting
import android.util.Base64
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request


class UserRepository {

    private val BASE_URL = "https://dragonball.keepcoding.education/api/"

    companion object {
        private var token = ""
    }

    sealed class LoginResponse {
        data object Success : LoginResponse()
        data class Error(val message: String) : LoginResponse()
    }

    fun login(username: String, password: String): LoginResponse {
        val client = OkHttpClient()
        val url = "${BASE_URL}auth/login"


        val credentials = "$username:$password"
        val authHeader = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        val request = Request.Builder()
            .url(url)
            .post(FormBody.Builder().build())
            .addHeader("Authorization", authHeader)
            .build()

        val call = client.newCall(request)
        val response = call.execute()

        return if (response.isSuccessful) {
            response.body?.use { responseBody ->
                val responseBodyString = responseBody.string()
                if (responseBodyString.isNotEmpty()) {
                    token = responseBodyString //Guardar el token
                    LoginResponse.Success
                } else {
                    LoginResponse.Error("Error: respuesta vacía")
                }
            } ?: LoginResponse.Error("Error: cuerpo de respuesta nulo")
        } else {
            LoginResponse.Error("Error en login: ${response.message}")
        }
    }

    
    fun getToken(): String = token

    @VisibleForTesting
    fun setToken(token: String) { UserRepository.token = token }
}