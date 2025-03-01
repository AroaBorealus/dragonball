package com.aroaborealus.dragonball.model

data class Character(val id: String, val nombre: String, val imagenUrl: String, var vidaActual: Int, val vidaTotal: Int = 100, var vecesSeleccionado: Int = 0)
