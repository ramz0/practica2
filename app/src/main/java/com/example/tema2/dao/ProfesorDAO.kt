package com.example.tema2.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tema2.db.AlumnoDBHelper
import com.example.tema2.model.Profesor

class ProfesorDAO(context: Context) : ICrud<Profesor> {

    private val dbHelper: AlumnoDBHelper = AlumnoDBHelper(context)

    override fun insertar(objeto: Profesor): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AlumnoDBHelper.COLUMN_PROFESOR_NOMBRE, objeto.nombre)
            put(AlumnoDBHelper.COLUMN_PROFESOR_APELLIDO, objeto.apellido)
            put(AlumnoDBHelper.COLUMN_PROFESOR_EDAD, objeto.edad)
            put(AlumnoDBHelper.COLUMN_PROFESOR_ESPECIALIDAD, objeto.especialidad)
            put(AlumnoDBHelper.COLUMN_PROFESOR_DEPARTAMENTO, objeto.departamento)
        }

        val id = db.insert(AlumnoDBHelper.TABLE_PROFESOR, null, values)
        db.close()
        return id
    }

    override fun actualizar(objeto: Profesor): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AlumnoDBHelper.COLUMN_PROFESOR_NOMBRE, objeto.nombre)
            put(AlumnoDBHelper.COLUMN_PROFESOR_APELLIDO, objeto.apellido)
            put(AlumnoDBHelper.COLUMN_PROFESOR_EDAD, objeto.edad)
            put(AlumnoDBHelper.COLUMN_PROFESOR_ESPECIALIDAD, objeto.especialidad)
            put(AlumnoDBHelper.COLUMN_PROFESOR_DEPARTAMENTO, objeto.departamento)
        }

        val rowsAffected = db.update(
            AlumnoDBHelper.TABLE_PROFESOR,
            values,
            "${AlumnoDBHelper.COLUMN_PROFESOR_ID} = ?",
            arrayOf(objeto.id.toString())
        )

        db.close()
        return rowsAffected
    }

    override fun eliminar(id: Int): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val rowsAffected = db.delete(
            AlumnoDBHelper.TABLE_PROFESOR,
            "${AlumnoDBHelper.COLUMN_PROFESOR_ID} = ?",
            arrayOf(id.toString())
        )

        db.close()
        return rowsAffected
    }

    override fun obtenerPorId(id: Int): Profesor? {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            AlumnoDBHelper.TABLE_PROFESOR,
            null,
            "${AlumnoDBHelper.COLUMN_PROFESOR_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var profesor: Profesor? = null
        if (cursor.moveToFirst()) {
            profesor = cursorToProfesor(cursor)
        }

        cursor.close()
        db.close()
        return profesor
    }

    override fun obtenerTodos(): List<Profesor> {
        val profesores = mutableListOf<Profesor>()
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            AlumnoDBHelper.TABLE_PROFESOR,
            null,
            null,
            null,
            null,
            null,
            "${AlumnoDBHelper.COLUMN_PROFESOR_ID} ASC"
        )

        if (cursor.moveToFirst()) {
            do {
                val profesor = cursorToProfesor(cursor)
                profesores.add(profesor)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return profesores
    }

    fun eliminarTodos(): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val rowsAffected = db.delete(AlumnoDBHelper.TABLE_PROFESOR, null, null)
        db.close()
        return rowsAffected
    }

    private fun cursorToProfesor(cursor: Cursor): Profesor {
        return Profesor(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_PROFESOR_ID)),
            nombreProfesor = cursor.getString(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_PROFESOR_NOMBRE)),
            apellidoProfesor = cursor.getString(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_PROFESOR_APELLIDO)),
            edadProfesor = cursor.getInt(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_PROFESOR_EDAD)),
            especialidad = cursor.getString(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_PROFESOR_ESPECIALIDAD)),
            departamento = cursor.getString(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_PROFESOR_DEPARTAMENTO))
        )
    }
}
