package com.aroaborealus.dragonball.presentation.login

import android.content.SharedPreferences
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aroaborealus.dragonball.presentation.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class LoginViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<State>(State.Idle)
    val uiState: StateFlow<State> = _uiState

    sealed class State {
        data object Idle : State()
        data object Loading : State()
        data class Success(val token: String) : State()
        data class Error(val message: String, val errorCode: Int) : State()
    }


    fun iniciarLogin(usuario: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = State.Loading
            delay(2000L)
            _uiState.value = State.Success("------ token ---------")
        }
    }

    fun guardarUsuario(preferences: SharedPreferences?, usuario: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000L)
            preferences?.edit()?.apply {
                putString("Usuario", usuario)
                putString("Pasword", password)
                apply()
            }
        }
    }



}