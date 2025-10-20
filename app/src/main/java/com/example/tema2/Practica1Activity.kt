package com.example.tema2

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tema2.databinding.ActivityPractica1Binding

class Practica1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPractica1Binding
    private val PREFS_NAME = "datos"
    private val KEY_NOMBRE = "nombre"
    private val KEY_EDAD = "edad"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPractica1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnGuardar.setOnClickListener {
            guardarDatos()
        }

        binding.btnLeer.setOnClickListener {
            leerDatos()
        }
    }

    private fun guardarDatos() {
        val nombre = binding.etNombre.text.toString()
        val edadStr = binding.etEdad.text.toString()

        if (nombre.isEmpty() || edadStr.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val edad = edadStr.toIntOrNull()
        if (edad == null) {
            Toast.makeText(this, "La edad debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Guardar en SharedPreferences
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_NOMBRE, nombre)
        editor.putInt(KEY_EDAD, edad)
        editor.apply()

        Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()

        // Limpiar campos
        binding.etNombre.text?.clear()
        binding.etEdad.text?.clear()
    }

    private fun leerDatos() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val nombre = sharedPreferences.getString(KEY_NOMBRE, null)
        val edad = sharedPreferences.getInt(KEY_EDAD, -1)

        if (nombre == null || edad == -1) {
            binding.tvResultado.text = "No hay datos guardados"
            Toast.makeText(this, "No hay datos guardados", Toast.LENGTH_SHORT).show()
        } else {
            val resultado = "Nombre: $nombre\nEdad: $edad años"
            binding.tvResultado.text = resultado
            Toast.makeText(this, "Datos cargados correctamente", Toast.LENGTH_SHORT).show()
        }
    }
}
