package com.example.tema2

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tema2.databinding.ActivityPractica2Binding
import java.io.FileNotFoundException

class Practica2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPractica2Binding
    private val FILENAME = "nota.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPractica2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnEscribir.setOnClickListener {
            escribirArchivo()
        }

        binding.btnLeer.setOnClickListener {
            leerArchivo()
        }
    }

    private fun escribirArchivo() {
        val texto = binding.etTexto.text.toString()

        if (texto.isEmpty()) {
            Toast.makeText(this, "Por favor escribe algo antes de guardar", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val fileOutput = openFileOutput(FILENAME, Context.MODE_PRIVATE)
            fileOutput.write(texto.toByteArray())
            fileOutput.close()

            Toast.makeText(this, "Texto guardado en archivo correctamente", Toast.LENGTH_SHORT).show()
            binding.etTexto.text?.clear()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun leerArchivo() {
        try {
            val fileInput = openFileInput(FILENAME)
            val contenido = fileInput.bufferedReader().use { it.readText() }
            fileInput.close()

            if (contenido.isNotEmpty()) {
                binding.tvResultado.text = contenido
                Toast.makeText(this, "Archivo leído correctamente", Toast.LENGTH_SHORT).show()
            } else {
                binding.tvResultado.text = "El archivo está vacío"
                Toast.makeText(this, "El archivo está vacío", Toast.LENGTH_SHORT).show()
            }
        } catch (e: FileNotFoundException) {
            binding.tvResultado.text = "No se encontró ningún archivo guardado"
            Toast.makeText(this, "No hay archivo guardado aún", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            binding.tvResultado.text = "Error al leer el archivo"
            Toast.makeText(this, "Error al leer: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
