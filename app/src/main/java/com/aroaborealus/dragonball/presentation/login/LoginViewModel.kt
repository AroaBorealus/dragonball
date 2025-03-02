package com.aroaborealus.dragonball.presentation.login

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aroaborealus.dragonball.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<State>(State.Idle)
    val uiState: StateFlow<State> = _uiState

    private val userRepository = UserRepository()

    sealed class State {
        data object Idle : State()
        data object Loading : State()
        data object Success : State()
        data class Error(val message: String, val errorCode: Int) : State()
    }


    fun iniciarLogin(usuario: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = State.Loading
            //delay(2000L)
            val loginResponse = userRepository.login(usuario, password)
            when (loginResponse) {
                is UserRepository.LoginResponse.Success -> {
                    _uiState.value = State.Success
                }
                is UserRepository.LoginResponse.Error -> {
                    _uiState.value = State.Error("Error con la contraseña o la conexión a internet", 401)
                }
            }
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

    // TODO mejoras.
    //  Si el usuario ya ha hecho login, entonces no volverselo a pedir
    //  Si el usuario le ha dado a recordar usuario y contraseña. Después mostrarlo
}