package com.example.tema2.model

open class Persona(
    val nombre: String,
    val apellido: String,
    val edad: Int
) {
    fun nombreCompleto(): String {
        return "$nombre $apellido"
    }
}
