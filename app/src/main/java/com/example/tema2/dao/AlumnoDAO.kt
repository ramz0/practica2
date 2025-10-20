package com.example.tema2.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tema2.db.AlumnoDBHelper
import com.example.tema2.model.Alumno

class AlumnoDAO(context: Context) : ICrud<Alumno> {

    private val dbHelper: AlumnoDBHelper = AlumnoDBHelper(context)

    /**
     * Inserta un nuevo alumno en la base de datos
     */
    override fun insertar(objeto: Alumno): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AlumnoDBHelper.COLUMN_ALUMNO_NOMBRE, objeto.nombre)
            put(AlumnoDBHelper.COLUMN_ALUMNO_APELLIDO, objeto.apellido)
            put(AlumnoDBHelper.COLUMN_ALUMNO_EDAD, objeto.edad)
            put(AlumnoDBHelper.COLUMN_ALUMNO_CARRERA, objeto.carrera)
        }

        val id = db.insert(AlumnoDBHelper.TABLE_ALUMNO, null, values)
        db.close()
        return id
    }

    /**
     * Actualiza un alumno existente
     */
    override fun actualizar(objeto: Alumno): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AlumnoDBHelper.COLUMN_ALUMNO_NOMBRE, objeto.nombre)
            put(AlumnoDBHelper.COLUMN_ALUMNO_APELLIDO, objeto.apellido)
            put(AlumnoDBHelper.COLUMN_ALUMNO_EDAD, objeto.edad)
            put(AlumnoDBHelper.COLUMN_ALUMNO_CARRERA, objeto.carrera)
        }

        val rowsAffected = db.update(
            AlumnoDBHelper.TABLE_ALUMNO,
            values,
            "${AlumnoDBHelper.COLUMN_ALUMNO_ID} = ?",
            arrayOf(objeto.id.toString())
        )

        db.close()
        return rowsAffected
    }

    /**
     * Elimina un alumno por su ID
     */
    override fun eliminar(id: Int): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val rowsAffected = db.delete(
            AlumnoDBHelper.TABLE_ALUMNO,
            "${AlumnoDBHelper.COLUMN_ALUMNO_ID} = ?",
            arrayOf(id.toString())
        )

        db.close()
        return rowsAffected
    }

    /**
     * Obtiene un alumno por su ID
     */
    override fun obtenerPorId(id: Int): Alumno? {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            AlumnoDBHelper.TABLE_ALUMNO,
            null,
            "${AlumnoDBHelper.COLUMN_ALUMNO_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var alumno: Alumno? = null
        if (cursor.moveToFirst()) {
            alumno = cursorToAlumno(cursor)
        }

        cursor.close()
        db.close()
        return alumno
    }

    /**
     * Obtiene todos los alumnos de la base de datos
     */
    override fun obtenerTodos(): List<Alumno> {
        val alumnos = mutableListOf<Alumno>()
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            AlumnoDBHelper.TABLE_ALUMNO,
            null,
            null,
            null,
            null,
            null,
            "${AlumnoDBHelper.COLUMN_ALUMNO_ID} ASC"
        )

        if (cursor.moveToFirst()) {
            do {
                val alumno = cursorToAlumno(cursor)
                alumnos.add(alumno)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return alumnos
    }

    /**
     * Elimina todos los alumnos de la base de datos
     */
    fun eliminarTodos(): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val rowsAffected = db.delete(AlumnoDBHelper.TABLE_ALUMNO, null, null)
        db.close()
        return rowsAffected
    }

    /**
     * Convierte un cursor en un objeto Alumno
     */
    private fun cursorToAlumno(cursor: Cursor): Alumno {
        return Alumno(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_ALUMNO_ID)),
            nombreAlumno = cursor.getString(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_ALUMNO_NOMBRE)),
            apellidoAlumno = cursor.getString(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_ALUMNO_APELLIDO)),
            edadAlumno = cursor.getInt(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_ALUMNO_EDAD)),
            carrera = cursor.getString(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_ALUMNO_CARRERA))
        )
    }
}
