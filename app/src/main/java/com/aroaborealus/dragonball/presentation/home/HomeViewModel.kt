package com.aroaborealus.dragonball.presentation.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.aroaborealus.dragonball.model.Character
import com.aroaborealus.dragonball.presentation.login.LoginViewModel.State
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HomeViewModel: ViewModel() {


    private val BASE_URL = "https://dragonball.keepcoding.education/api/"
    private var token = ""

    sealed class State {
        data object Loading: State()
        data class Success(val characters: List<Character>): State()
        data class Error(val message: String, val errorCode: Int) : State()
        data class SelectedCharacter(val character: Character): State()
    }

    private val _uiState = MutableStateFlow<State>(State.Loading)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    fun actualizarToken(token: String) {
        this.token = token
    }

    fun selectedCharacter(character: Character) {
        _uiState.value = State.SelectedCharacter(character)
    }

    fun getCharactersMock() {
        _uiState.value = State.Loading
        _uiState.value = State.Success(listOf(Character("id1234","Meu","https://cdn.alfabetajuega.com/alfabetajuega/2020/12/goku1.jpg?width=300",100),
                                              Character("Qwerty1","Miu","foto.com",100)))
    }

    fun getCharacters() {
        viewModelScope.launch {
            _uiState.value = State.Loading

            var client = OkHttpClient()
            val url = "${BASE_URL}heros/all"

            val formBody = FormBody.Builder()
                .add("name", "")
                .build()

            val request = Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("Authorization", "Bearer $token")
                .build()

            val call = client.newCall(request)
            val response = call.execute()

            if (response.isSuccessful) {
                // TODO analizar la respuesta que viene en json y pasarlo a lista
                _uiState.value = State.Success(listOf())
            } else {
                _uiState.value = State.Error("Error al descargar los personajes. ${response.message}",
                                             402)
            }
        }
    }


    fun descargarPersonajesAlternativo() {
        viewModelScope.launch {
            _uiState.value = State.Loading

            var client = OkHttpClient()
            val url = "${BASE_URL}heros/all"

            val formBody = FormBody.Builder()
                .add("name", "")
                .build()

            val request = Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("Authorization", "Bearer $token")
                .build()

            val call = client.newCall(request)
            val response = call.enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        _uiState.value = State.Error("Error",
                                                     401)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        // TODO analizar la respuesta que viene en json y pasarlo a lista
                        _uiState.value = State.Success(listOf())
                    }

                }
            )


        }
    }

}