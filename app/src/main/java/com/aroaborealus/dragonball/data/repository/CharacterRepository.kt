package com.aroaborealus.dragonball.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.aroaborealus.dragonball.model.Character
import com.aroaborealus.dragonball.model.CharacterDTO
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class CharacterRepository {

    private val BASE_URL = "https://dragonball.keepcoding.education/api/"
    private var listaPersonajes = listOf<Character>()

    sealed class PersonajesResponse {
        data class Success(val personajes: List<Character>) : PersonajesResponse()
        data class Error(val message: String) : PersonajesResponse()
    }

    fun fetchPersonajes(token: String, sharedPreferences: SharedPreferences? = null): PersonajesResponse {
        if (listaPersonajes.isNotEmpty())
            return PersonajesResponse.Success(listaPersonajes)

        // Este es un ejemplo de como guardar en las shared preferences toda la lista de personajes. Falta que se actualice cuando reciban golpes
        sharedPreferences?.let {
            val listaPersonajesJson = it.getString("listaPersonajes", "")
            val personajes: Array<Character>? =
                Gson().fromJson(listaPersonajesJson, Array<Character>::class.java)
            if(!personajes.isNullOrEmpty()) return PersonajesResponse.Success(personajes.toList())
        }
        // TODO para completar el vecesSeleccionado.
        //  tendremos que guardar en las sharedPreferences la lista de personajes con todos sus datos.
        //  antes de llamar a internet, comprobar en las sharedPreferences. Si no hay nada, vamos a internet.
        //  si tenemos datos previos, lo cargamos de las preferencias

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

        val call = client.newCall(request)
        val response = call.execute()

        return if (response.isSuccessful) {
            val personajesDto: Array<CharacterDTO> =
                Gson().fromJson(response.body?.string(), Array<CharacterDTO>::class.java)
            // Aqui hemos descargado la lista

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

    }

}