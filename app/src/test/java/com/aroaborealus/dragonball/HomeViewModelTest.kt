package com.aroaborealus.dragonball

import android.content.SharedPreferences
import app.cash.turbine.test
import com.aroaborealus.dragonball.presentation.home.HomeViewModel
import com.aroaborealus.dragonball.presentation.home.HomeViewModel.State
import com.aroaborealus.dragonball.data.repository.CharacterRepository
import com.aroaborealus.dragonball.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val viewModel = HomeViewModel(true)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }


    val personajeInicial = Character(
        id = "1",
        nombre = "Nombre",
        imagenUrl = "----",
        vidaTotal = 100,
        vidaActual = 100,
        vecesSeleccionado = 0,
    )


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    //Para testear diferentes métodos de la clase, habría usado un mock pero como no podemos
    // empleé como alternativa el parámetro isTesting en el momento de crear el ViewModel

    @Test
    fun `golpearPersonaje baja la vida del personaje entre 10 y 60 random`() {
        viewModel.golpearPersonaje(personajeInicial,null)
        assertTrue(personajeInicial.vidaActual >=40)
        assertTrue(personajeInicial.vidaActual <=90)
    }

    @Test
    fun `curarPersonaje sube la vida del personaje en 20`() {
        val personajeEsperado = personajeInicial.copy(
            vidaActual = 60
        )
        viewModel.curarPersonaje(personajeEsperado,null)
        assertTrue(personajeEsperado.vidaActual == 80)
    }

    @Test
    fun `cuando se selecciona un personaje, el state se actualiza a PersonajeSelecciado con ese personaje`() = runTest {
        viewModel.uiState.test {
            assertEquals(State.Loading, awaitItem())
            viewModel.personajeSeleccionado(personajeInicial)
            assertEquals(State.PersonajeSeleccionado(personajeInicial), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `cuando se deselecciona un personaje, el state se actualiza a Success`() = runTest {
        viewModel.uiState.test {
            viewModel.userRepository.setToken("eyJraWQiOiJwcml2YXRlIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJleHBpcmF0aW9uIjo2NDA5MjIxMTIwMCwiZW1haWwiOiJjZHRsQHBydWVibWFpbC5lcyIsImlkZW50aWZ5IjoiRDIwRTAwQTktODY0NC00MUYyLUE0OUYtN0ZDRUY2MTVFMTQ3In0.wMqJfh5qcs5tU6hu2VxT4OV9Svd7BGBA7HsVpKhx5-8")
            assertEquals(State.Loading, awaitItem())
            viewModel.personajeDeseleccionado(null)
            assertTrue(awaitItem() is State.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }

}