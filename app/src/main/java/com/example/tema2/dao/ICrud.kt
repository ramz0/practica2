package com.example.tema2.dao

interface ICrud<T> {
    /**
     * Inserta un nuevo elemento en la base de datos
     * @param objeto El objeto a insertar
     * @return El ID del registro insertado, o -1 si falla
     */
    fun insertar(objeto: T): Long

    /**
     * Actualiza un elemento existente en la base de datos
     * @param objeto El objeto a actualizar
     * @return Número de filas afectadas
     */
    fun actualizar(objeto: T): Int

    /**
     * Elimina un elemento de la base de datos por su ID
     * @param id El ID del elemento a eliminar
     * @return Número de filas afectadas
     */
    fun eliminar(id: Int): Int

    /**
     * Obtiene un elemento por su ID
     * @param id El ID del elemento a buscar
     * @return El objeto encontrado, o null si no existe
     */
    fun obtenerPorId(id: Int): T?

    /**
     * Obtiene todos los elementos de la base de datos
     * @return Lista de todos los elementos
     */
    fun obtenerTodos(): List<T>
}
