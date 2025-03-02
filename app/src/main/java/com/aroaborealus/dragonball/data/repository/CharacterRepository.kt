package com.aroaborealus.dragonball.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.aroaborealus.dragonball.model.Character
import com.aroaborealus.dragonball.model.CharacterDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class CharacterRepository(private val isTesting: Boolean = false) {



    private val BASE_URL = "https://dragonball.keepcoding.education/api/"
    private var listaPersonajes = listOf<Character>()

    sealed class PersonajesResponse {
        data class Success(val personajes: List<Character>) : PersonajesResponse()
        data class Error(val message: String) : PersonajesResponse()
    }

    suspend fun fetchPersonajes(token: String, sharedPreferences: SharedPreferences? = null): PersonajesResponse {
        if (listaPersonajes.isNotEmpty())
            return PersonajesResponse.Success(listaPersonajes)

        if(isTesting){
            val characterList: List<Character> = listOf(Character("01","Goku","goku.com",100),
                                                        Character("02","Vegeta","vegeta.com",100))
            return PersonajesResponse.Success(characterList)
        }else{
            sharedPreferences?.let {
                val listaPersonajesJson = it.getString("listaPersonajes", "")
                val personajes: Array<Character>? =
                    Gson().fromJson(listaPersonajesJson, Array<Character>::class.java)
                if (!personajes.isNullOrEmpty()) return PersonajesResponse.Success(personajes.toList())
            }
        }

        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val url = "${BASE_URL}heros/all"

            val formBody = FormBody.Builder()
                .add("name", "")
                .build()

            val request = Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("Authorization", "Bearer $token")
                .build()

            try {
                val call = client.newCall(request)
                val response = call.execute()

                if (response.isSuccessful) {
                    val personajesDto: Array<CharacterDTO> =
                        Gson().fromJson(response.body?.string(), Array<CharacterDTO>::class.java)

                    listaPersonajes = personajesDto.map {
                        Character(
                            id = it.id,
                            nombre = it.name,
                            imagenUrl = it.photo,
                            vidaActual = 100,
                            vidaTotal = 100,
                        )
                    }

                    sharedPreferences?.edit()?.apply {
                        putString("listaPersonajes", Gson().toJson(listaPersonajes))
                        apply()
                    }

                    PersonajesResponse.Success(listaPersonajes)
                } else {
                    PersonajesResponse.Error("Error al descargar los personajes. ${response.message}")
                }
            } catch (e: Exception) {
                PersonajesResponse.Error("Excepción al realizar la solicitud: ${e.localizedMessage}")
            }
        }
    }
}
