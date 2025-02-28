package com.aroaborealus.dragonball.presentation.home

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.aroaborealus.dragonball.data.repository.CharacterRepository
import com.aroaborealus.dragonball.data.repository.UserRepository
import com.aroaborealus.dragonball.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting

class HomeViewModel: ViewModel() {

    sealed class State {
        data object Loading: State()
        data class Success(val personajes: List<Character>): State()
        data class Error(val message: String): State()
        data class PersonajeSeleccionado(val personaje: Character): State()
    }

    private val _uiState = MutableStateFlow<State>(State.Loading)
    private val personajeRepository = CharacterRepository()
    @VisibleForTesting
    val userRepository = UserRepository()

    val uiState: StateFlow<State> = _uiState.asStateFlow()

    fun golpearPersonaje(personaje: Character) {
        personaje.vidaActual -= 10
    }

    fun personajeSeleccionado(personaje: Character) {
        personaje.vecesSeleccionado++
        _uiState.value = State.PersonajeSeleccionado(personaje)
    }

    fun personajeDeseleccionado() {
        val resultado = personajeRepository.fetchPersonajes(userRepository.getToken(),)
        when (resultado) {
            is CharacterRepository.PersonajesResponse.Success -> {
                _uiState.value = State.Success(resultado.personajes)
            }

            is CharacterRepository.PersonajesResponse.Error -> {
                _uiState.value = State.Error(resultado.message)
            }
        }
    }

    fun descargarPersonajes(sharedPreferences: SharedPreferences) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = State.Loading
            val resultado = personajeRepository.fetchPersonajes(userRepository.getToken(), sharedPreferences)
            when (resultado) {
                is CharacterRepository.PersonajesResponse.Success -> {
                    _uiState.value = State.Success(resultado.personajes)
                }

                is CharacterRepository.PersonajesResponse.Error -> {
                    _uiState.value = State.Error(resultado.message)
                }
            }
        }
    }


}