package com.example.tema2.model

data class Falla(
    val id: Int = 0,
    val alumnoId: Int,
    val nombreAlumno: String,
    val fecha: String,
    val materia: String,
    val tipo: String // "Falta", "Retardo", "Justificada"
) {
    override fun toString(): String {
        return "Falla(id=$id, alumno='$nombreAlumno', fecha='$fecha', materia='$materia', tipo='$tipo')"
    }
}
