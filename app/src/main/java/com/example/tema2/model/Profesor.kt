package com.example.tema2.model

data class Profesor(
    val id: Int = 0,
    val nombreProfesor: String,
    val apellidoProfesor: String,
    val edadProfesor: Int,
    val especialidad: String,
    val departamento: String
) : Persona(nombreProfesor, apellidoProfesor, edadProfesor) {

    override fun toString(): String {
        return "Profesor(id=$id, nombre=${nombreCompleto()}, edad=$edad, especialidad='$especialidad', departamento='$departamento')"
    }
}
