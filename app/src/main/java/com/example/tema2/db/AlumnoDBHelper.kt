package com.example.tema2.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AlumnoDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Configuración de la base de datos
        private const val DATABASE_NAME = "escuela.db"
        private const val DATABASE_VERSION = 2

        // Tabla Alumno
        const val TABLE_ALUMNO = "alumno"
        const val COLUMN_ALUMNO_ID = "id"
        const val COLUMN_ALUMNO_NOMBRE = "nombre"
        const val COLUMN_ALUMNO_APELLIDO = "apellido"
        const val COLUMN_ALUMNO_EDAD = "edad"
        const val COLUMN_ALUMNO_CARRERA = "carrera"

        // Tabla Profesor
        const val TABLE_PROFESOR = "profesor"
        const val COLUMN_PROFESOR_ID = "id"
        const val COLUMN_PROFESOR_NOMBRE = "nombre"
        const val COLUMN_PROFESOR_APELLIDO = "apellido"
        const val COLUMN_PROFESOR_EDAD = "edad"
        const val COLUMN_PROFESOR_ESPECIALIDAD = "especialidad"
        const val COLUMN_PROFESOR_DEPARTAMENTO = "departamento"

        // Tabla Falla
        const val TABLE_FALLA = "falla"
        const val COLUMN_FALLA_ID = "id"
        const val COLUMN_FALLA_ALUMNO_ID = "alumno_id"
        const val COLUMN_FALLA_NOMBRE_ALUMNO = "nombre_alumno"
        const val COLUMN_FALLA_FECHA = "fecha"
        const val COLUMN_FALLA_MATERIA = "materia"
        const val COLUMN_FALLA_TIPO = "tipo"

        // Tabla Curso
        const val TABLE_CURSO = "curso"
        const val COLUMN_CURSO_ID = "id"
        const val COLUMN_CURSO_NOMBRE = "nombre_curso"
        const val COLUMN_CURSO_CODIGO = "codigo"
        const val COLUMN_CURSO_CREDITOS = "creditos"

        // SQL para crear tabla Alumno
        private const val CREATE_TABLE_ALUMNO = """
            CREATE TABLE $TABLE_ALUMNO (
                $COLUMN_ALUMNO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ALUMNO_NOMBRE TEXT NOT NULL,
                $COLUMN_ALUMNO_APELLIDO TEXT NOT NULL,
                $COLUMN_ALUMNO_EDAD INTEGER NOT NULL,
                $COLUMN_ALUMNO_CARRERA TEXT NOT NULL
            )
        """

        // SQL para crear tabla Profesor
        private const val CREATE_TABLE_PROFESOR = """
            CREATE TABLE $TABLE_PROFESOR (
                $COLUMN_PROFESOR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PROFESOR_NOMBRE TEXT NOT NULL,
                $COLUMN_PROFESOR_APELLIDO TEXT NOT NULL,
                $COLUMN_PROFESOR_EDAD INTEGER NOT NULL,
                $COLUMN_PROFESOR_ESPECIALIDAD TEXT NOT NULL,
                $COLUMN_PROFESOR_DEPARTAMENTO TEXT NOT NULL
            )
        """

        // SQL para crear tabla Falla
        private const val CREATE_TABLE_FALLA = """
            CREATE TABLE $TABLE_FALLA (
                $COLUMN_FALLA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FALLA_ALUMNO_ID INTEGER NOT NULL,
                $COLUMN_FALLA_NOMBRE_ALUMNO TEXT NOT NULL,
                $COLUMN_FALLA_FECHA TEXT NOT NULL,
                $COLUMN_FALLA_MATERIA TEXT NOT NULL,
                $COLUMN_FALLA_TIPO TEXT NOT NULL
            )
        """

        // SQL para crear tabla Curso
        private const val CREATE_TABLE_CURSO = """
            CREATE TABLE $TABLE_CURSO (
                $COLUMN_CURSO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CURSO_NOMBRE TEXT NOT NULL,
                $COLUMN_CURSO_CODIGO TEXT NOT NULL UNIQUE,
                $COLUMN_CURSO_CREDITOS INTEGER NOT NULL
            )
        """

        // SQL para eliminar tablas
        private const val DROP_TABLE_ALUMNO = "DROP TABLE IF EXISTS $TABLE_ALUMNO"
        private const val DROP_TABLE_PROFESOR = "DROP TABLE IF EXISTS $TABLE_PROFESOR"
        private const val DROP_TABLE_FALLA = "DROP TABLE IF EXISTS $TABLE_FALLA"
        private const val DROP_TABLE_CURSO = "DROP TABLE IF EXISTS $TABLE_CURSO"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear todas las tablas
        db?.execSQL(CREATE_TABLE_ALUMNO)
        db?.execSQL(CREATE_TABLE_PROFESOR)
        db?.execSQL(CREATE_TABLE_FALLA)
        db?.execSQL(CREATE_TABLE_CURSO)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Eliminar tablas existentes y recrear
        db?.execSQL(DROP_TABLE_FALLA)
        db?.execSQL(DROP_TABLE_PROFESOR)
        db?.execSQL(DROP_TABLE_CURSO)
        db?.execSQL(DROP_TABLE_ALUMNO)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
}
