package com.example.tema2.model

data class Curso(
    val id: Int = 0,
    val nombreCurso: String,
    val codigo: String,
    val creditos: Int
) {
    override fun toString(): String {
        return "Curso(id=$id, nombre='$nombreCurso', código='$codigo', créditos=$creditos)"
    }
}
