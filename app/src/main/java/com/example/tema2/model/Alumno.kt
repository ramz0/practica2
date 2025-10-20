package com.example.tema2.model

data class Alumno(
    val id: Int = 0,
    val nombreAlumno: String,
    val apellidoAlumno: String,
    val edadAlumno: Int,
    val carrera: String
) : Persona(nombreAlumno, apellidoAlumno, edadAlumno) {

    override fun toString(): String {
        return "Alumno(id=$id, nombre=${nombreCompleto()}, edad=$edad, carrera='$carrera')"
    }
}
