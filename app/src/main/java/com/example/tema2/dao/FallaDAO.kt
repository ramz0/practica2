package com.example.tema2.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tema2.db.AlumnoDBHelper
import com.example.tema2.model.Falla

class FallaDAO(context: Context) : ICrud<Falla> {

    private val dbHelper: AlumnoDBHelper = AlumnoDBHelper(context)

    override fun insertar(objeto: Falla): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AlumnoDBHelper.COLUMN_FALLA_ALUMNO_ID, objeto.alumnoId)
            put(AlumnoDBHelper.COLUMN_FALLA_NOMBRE_ALUMNO, objeto.nombreAlumno)
            put(AlumnoDBHelper.COLUMN_FALLA_FECHA, objeto.fecha)
            put(AlumnoDBHelper.COLUMN_FALLA_MATERIA, objeto.materia)
            put(AlumnoDBHelper.COLUMN_FALLA_TIPO, objeto.tipo)
        }

        val id = db.insert(AlumnoDBHelper.TABLE_FALLA, null, values)
        db.close()
        return id
    }

    override fun actualizar(objeto: Falla): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AlumnoDBHelper.COLUMN_FALLA_ALUMNO_ID, objeto.alumnoId)
            put(AlumnoDBHelper.COLUMN_FALLA_NOMBRE_ALUMNO, objeto.nombreAlumno)
            put(AlumnoDBHelper.COLUMN_FALLA_FECHA, objeto.fecha)
            put(AlumnoDBHelper.COLUMN_FALLA_MATERIA, objeto.materia)
            put(AlumnoDBHelper.COLUMN_FALLA_TIPO, objeto.tipo)
        }

        val rowsAffected = db.update(
            AlumnoDBHelper.TABLE_FALLA,
            values,
            "${AlumnoDBHelper.COLUMN_FALLA_ID} = ?",
            arrayOf(objeto.id.toString())
        )

        db.close()
        return rowsAffected
    }

    override fun eliminar(id: Int): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val rowsAffected = db.delete(
            AlumnoDBHelper.TABLE_FALLA,
            "${AlumnoDBHelper.COLUMN_FALLA_ID} = ?",
            arrayOf(id.toString())
        )

        db.close()
        return rowsAffected
    }

    override fun obtenerPorId(id: Int): Falla? {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            AlumnoDBHelper.TABLE_FALLA,
            null,
            "${AlumnoDBHelper.COLUMN_FALLA_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var falla: Falla? = null
        if (cursor.moveToFirst()) {
            falla = cursorToFalla(cursor)
        }

        cursor.close()
        db.close()
        return falla
    }

    override fun obtenerTodos(): List<Falla> {
        val fallas = mutableListOf<Falla>()
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            AlumnoDBHelper.TABLE_FALLA,
            null,
            null,
            null,
            null,
            null,
            "${AlumnoDBHelper.COLUMN_FALLA_FECHA} DESC"
        )

        if (cursor.moveToFirst()) {
            do {
                val falla = cursorToFalla(cursor)
                fallas.add(falla)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return fallas
    }

    fun obtenerPorAlumno(alumnoId: Int): List<Falla> {
        val fallas = mutableListOf<Falla>()
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            AlumnoDBHelper.TABLE_FALLA,
            null,
            "${AlumnoDBHelper.COLUMN_FALLA_ALUMNO_ID} = ?",
            arrayOf(alumnoId.toString()),
            null,
            null,
            "${AlumnoDBHelper.COLUMN_FALLA_FECHA} DESC"
        )

        if (cursor.moveToFirst()) {
            do {
                val falla = cursorToFalla(cursor)
                fallas.add(falla)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return fallas
    }

    fun eliminarTodos(): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val rowsAffected = db.delete(AlumnoDBHelper.TABLE_FALLA, null, null)
        db.close()
        return rowsAffected
    }

    private fun cursorToFalla(cursor: Cursor): Falla {
        return Falla(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_FALLA_ID)),
            alumnoId = cursor.getInt(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_FALLA_ALUMNO_ID)),
            nombreAlumno = cursor.getString(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_FALLA_NOMBRE_ALUMNO)),
            fecha = cursor.getString(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_FALLA_FECHA)),
            materia = cursor.getString(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_FALLA_MATERIA)),
            tipo = cursor.getString(cursor.getColumnIndexOrThrow(AlumnoDBHelper.COLUMN_FALLA_TIPO))
        )
    }
}
