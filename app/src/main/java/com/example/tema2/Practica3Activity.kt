package com.example.tema2

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tema2.dao.AlumnoDAO
import com.example.tema2.dao.FallaDAO
import com.example.tema2.dao.ProfesorDAO
import com.example.tema2.databinding.ActivityPractica3Binding
import com.example.tema2.model.Alumno
import com.example.tema2.model.Falla
import com.example.tema2.model.Profesor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Practica3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPractica3Binding
    private lateinit var alumnoDAO: AlumnoDAO
    private lateinit var profesorDAO: ProfesorDAO
    private lateinit var fallaDAO: FallaDAO

    // Para manejar la selección de alumnos en el Spinner
    private var alumnosList: List<Alumno> = listOf()
    private var alumnoSeleccionadoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPractica3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar DAOs
        alumnoDAO = AlumnoDAO(this)
        profesorDAO = ProfesorDAO(this)
        fallaDAO = FallaDAO(this)

        setupSectionButtons()
        setupAlumnosListeners()
        setupProfesoresListeners()
        setupFallasListeners()

        // Mostrar sección de Alumnos por defecto
        showSection("alumnos")
    }

    private fun setupSectionButtons() {
        binding.btnSeccionAlumnos.setOnClickListener {
            showSection("alumnos")
        }

        binding.btnSeccionProfesores.setOnClickListener {
            showSection("profesores")
        }

        binding.btnSeccionFallas.setOnClickListener {
            showSection("fallas")
        }
    }

    private fun showSection(section: String) {
        // Ocultar todas las secciones
        binding.layoutAlumnos.visibility = View.GONE
        binding.layoutProfesores.visibility = View.GONE
        binding.layoutFallas.visibility = View.GONE

        // Mostrar la sección seleccionada
        when (section) {
            "alumnos" -> binding.layoutAlumnos.visibility = View.VISIBLE
            "profesores" -> binding.layoutProfesores.visibility = View.VISIBLE
            "fallas" -> {
                binding.layoutFallas.visibility = View.VISIBLE
                cargarSpinnerAlumnos()
                configurarSpinnerTiposFalla()
                establecerFechaActual()
            }
        }

        binding.tvResultado.text = "Los resultados aparecerán aquí"
    }

    private fun establecerFechaActual() {
        val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        binding.etFallaFecha.setText(fechaActual)
    }

    private fun cargarSpinnerAlumnos() {
        alumnosList = alumnoDAO.obtenerTodos()

        if (alumnosList.isEmpty()) {
            Toast.makeText(this, "No hay alumnos registrados. Agrega alumnos primero.", Toast.LENGTH_LONG).show()
            val listaVacia = listOf("No hay alumnos disponibles")
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaVacia)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerAlumno.adapter = adapter
            return
        }

        val nombresAlumnos = alumnosList.map { "${it.nombreCompleto()} (ID: ${it.id})" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresAlumnos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerAlumno.adapter = adapter

        binding.spinnerAlumno.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (alumnosList.isNotEmpty()) {
                    alumnoSeleccionadoId = alumnosList[position].id
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                alumnoSeleccionadoId = 0
            }
        }
    }

    private fun configurarSpinnerTiposFalla() {
        val tiposFalla = listOf(
            "Daño en PC",
            "Proyector no funciona HDMI",
            "Teclado dañado",
            "Mouse no funciona",
            "No hay conexión a internet",
            "Software no instalado",
            "Pantalla rota",
            "Equipo no enciende"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposFalla)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTipoFalla.adapter = adapter
    }

    // ==================== ALUMNOS ====================

    private fun setupAlumnosListeners() {
        binding.btnAlumnoInsertar.setOnClickListener { insertarAlumno() }
        binding.btnAlumnoActualizar.setOnClickListener { actualizarAlumno() }
        binding.btnAlumnoEliminar.setOnClickListener { eliminarAlumno() }
        binding.btnAlumnoListar.setOnClickListener { listarAlumnos() }
        binding.btnAlumnoBuscar.setOnClickListener { buscarAlumno() }
    }

    private fun insertarAlumno() {
        val nombre = binding.etAlumnoNombre.text.toString()
        val apellido = binding.etAlumnoApellido.text.toString()
        val edadStr = binding.etAlumnoEdad.text.toString()
        val carrera = binding.etAlumnoCarrera.text.toString()

        if (nombre.isEmpty() || apellido.isEmpty() || edadStr.isEmpty() || carrera.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val edad = edadStr.toIntOrNull()
        if (edad == null) {
            Toast.makeText(this, "Edad inválida", Toast.LENGTH_SHORT).show()
            return
        }

        val alumno = Alumno(
            nombreAlumno = nombre,
            apellidoAlumno = apellido,
            edadAlumno = edad,
            carrera = carrera
        )

        val id = alumnoDAO.insertar(alumno)
        if (id > 0) {
            Toast.makeText(this, "Alumno insertado con ID: $id", Toast.LENGTH_SHORT).show()
            limpiarCamposAlumno()
        } else {
            Toast.makeText(this, "Error al insertar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarAlumno() {
        val idStr = binding.etAlumnoId.text.toString()
        val nombre = binding.etAlumnoNombre.text.toString()
        val apellido = binding.etAlumnoApellido.text.toString()
        val edadStr = binding.etAlumnoEdad.text.toString()
        val carrera = binding.etAlumnoCarrera.text.toString()

        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingresa el ID", Toast.LENGTH_SHORT).show()
            return
        }

        if (nombre.isEmpty() || apellido.isEmpty() || edadStr.isEmpty() || carrera.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val id = idStr.toIntOrNull()
        val edad = edadStr.toIntOrNull()

        if (id == null || edad == null) {
            Toast.makeText(this, "ID o edad inválidos", Toast.LENGTH_SHORT).show()
            return
        }

        val alumno = Alumno(id, nombre, apellido, edad, carrera)
        val rowsAffected = alumnoDAO.actualizar(alumno)

        if (rowsAffected > 0) {
            Toast.makeText(this, "Alumno actualizado", Toast.LENGTH_SHORT).show()
            limpiarCamposAlumno()
        } else {
            Toast.makeText(this, "No se encontró el alumno", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarAlumno() {
        val idStr = binding.etAlumnoId.text.toString()

        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingresa el ID", Toast.LENGTH_SHORT).show()
            return
        }

        val id = idStr.toIntOrNull()
        if (id == null) {
            Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val rowsAffected = alumnoDAO.eliminar(id)
        if (rowsAffected > 0) {
            Toast.makeText(this, "Alumno eliminado", Toast.LENGTH_SHORT).show()
            limpiarCamposAlumno()
        } else {
            Toast.makeText(this, "No se encontró el alumno", Toast.LENGTH_SHORT).show()
        }
    }

    private fun listarAlumnos() {
        val alumnos = alumnoDAO.obtenerTodos()

        if (alumnos.isEmpty()) {
            binding.tvResultado.text = "No hay alumnos registrados"
        } else {
            val resultado = StringBuilder("=== ALUMNOS (${alumnos.size}) ===\n\n")
            alumnos.forEach { alumno ->
                resultado.append("ID: ${alumno.id}\n")
                resultado.append("Nombre: ${alumno.nombreCompleto()}\n")
                resultado.append("Edad: ${alumno.edad} años\n")
                resultado.append("Carrera: ${alumno.carrera}\n")
                resultado.append("------------------------\n")
            }
            binding.tvResultado.text = resultado.toString()
        }
    }

    private fun buscarAlumno() {
        val idStr = binding.etAlumnoId.text.toString()

        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingresa el ID", Toast.LENGTH_SHORT).show()
            return
        }

        val id = idStr.toIntOrNull()
        if (id == null) {
            Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val alumno = alumnoDAO.obtenerPorId(id)

        if (alumno != null) {
            binding.etAlumnoNombre.setText(alumno.nombre)
            binding.etAlumnoApellido.setText(alumno.apellido)
            binding.etAlumnoEdad.setText(alumno.edad.toString())
            binding.etAlumnoCarrera.setText(alumno.carrera)

            binding.tvResultado.text = "Alumno encontrado:\n\n${alumno.toString()}"
            Toast.makeText(this, "Alumno cargado", Toast.LENGTH_SHORT).show()
        } else {
            binding.tvResultado.text = "No se encontró alumno con ID: $id"
        }
    }

    private fun limpiarCamposAlumno() {
        binding.etAlumnoId.text?.clear()
        binding.etAlumnoNombre.text?.clear()
        binding.etAlumnoApellido.text?.clear()
        binding.etAlumnoEdad.text?.clear()
        binding.etAlumnoCarrera.text?.clear()
    }

    // ==================== PROFESORES ====================

    private fun setupProfesoresListeners() {
        binding.btnProfesorInsertar.setOnClickListener { insertarProfesor() }
        binding.btnProfesorActualizar.setOnClickListener { actualizarProfesor() }
        binding.btnProfesorEliminar.setOnClickListener { eliminarProfesor() }
        binding.btnProfesorListar.setOnClickListener { listarProfesores() }
        binding.btnProfesorBuscar.setOnClickListener { buscarProfesor() }
    }

    private fun insertarProfesor() {
        val nombre = binding.etProfesorNombre.text.toString()
        val apellido = binding.etProfesorApellido.text.toString()
        val edadStr = binding.etProfesorEdad.text.toString()
        val especialidad = binding.etProfesorEspecialidad.text.toString()
        val departamento = binding.etProfesorDepartamento.text.toString()

        if (nombre.isEmpty() || apellido.isEmpty() || edadStr.isEmpty() ||
            especialidad.isEmpty() || departamento.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val edad = edadStr.toIntOrNull()
        if (edad == null) {
            Toast.makeText(this, "Edad inválida", Toast.LENGTH_SHORT).show()
            return
        }

        val profesor = Profesor(
            nombreProfesor = nombre,
            apellidoProfesor = apellido,
            edadProfesor = edad,
            especialidad = especialidad,
            departamento = departamento
        )

        val id = profesorDAO.insertar(profesor)
        if (id > 0) {
            Toast.makeText(this, "Profesor insertado con ID: $id", Toast.LENGTH_SHORT).show()
            limpiarCamposProfesor()
        } else {
            Toast.makeText(this, "Error al insertar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarProfesor() {
        val idStr = binding.etProfesorId.text.toString()
        val nombre = binding.etProfesorNombre.text.toString()
        val apellido = binding.etProfesorApellido.text.toString()
        val edadStr = binding.etProfesorEdad.text.toString()
        val especialidad = binding.etProfesorEspecialidad.text.toString()
        val departamento = binding.etProfesorDepartamento.text.toString()

        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingresa el ID", Toast.LENGTH_SHORT).show()
            return
        }

        if (nombre.isEmpty() || apellido.isEmpty() || edadStr.isEmpty() ||
            especialidad.isEmpty() || departamento.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val id = idStr.toIntOrNull()
        val edad = edadStr.toIntOrNull()

        if (id == null || edad == null) {
            Toast.makeText(this, "ID o edad inválidos", Toast.LENGTH_SHORT).show()
            return
        }

        val profesor = Profesor(id, nombre, apellido, edad, especialidad, departamento)
        val rowsAffected = profesorDAO.actualizar(profesor)

        if (rowsAffected > 0) {
            Toast.makeText(this, "Profesor actualizado", Toast.LENGTH_SHORT).show()
            limpiarCamposProfesor()
        } else {
            Toast.makeText(this, "No se encontró el profesor", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarProfesor() {
        val idStr = binding.etProfesorId.text.toString()

        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingresa el ID", Toast.LENGTH_SHORT).show()
            return
        }

        val id = idStr.toIntOrNull()
        if (id == null) {
            Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val rowsAffected = profesorDAO.eliminar(id)
        if (rowsAffected > 0) {
            Toast.makeText(this, "Profesor eliminado", Toast.LENGTH_SHORT).show()
            limpiarCamposProfesor()
        } else {
            Toast.makeText(this, "No se encontró el profesor", Toast.LENGTH_SHORT).show()
        }
    }

    private fun listarProfesores() {
        val profesores = profesorDAO.obtenerTodos()

        if (profesores.isEmpty()) {
            binding.tvResultado.text = "No hay profesores registrados"
        } else {
            val resultado = StringBuilder("=== PROFESORES (${profesores.size}) ===\n\n")
            profesores.forEach { profesor ->
                resultado.append("ID: ${profesor.id}\n")
                resultado.append("Nombre: ${profesor.nombreCompleto()}\n")
                resultado.append("Edad: ${profesor.edad} años\n")
                resultado.append("Especialidad: ${profesor.especialidad}\n")
                resultado.append("Departamento: ${profesor.departamento}\n")
                resultado.append("------------------------\n")
            }
            binding.tvResultado.text = resultado.toString()
        }
    }

    private fun buscarProfesor() {
        val idStr = binding.etProfesorId.text.toString()

        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingresa el ID", Toast.LENGTH_SHORT).show()
            return
        }

        val id = idStr.toIntOrNull()
        if (id == null) {
            Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val profesor = profesorDAO.obtenerPorId(id)

        if (profesor != null) {
            binding.etProfesorNombre.setText(profesor.nombre)
            binding.etProfesorApellido.setText(profesor.apellido)
            binding.etProfesorEdad.setText(profesor.edad.toString())
            binding.etProfesorEspecialidad.setText(profesor.especialidad)
            binding.etProfesorDepartamento.setText(profesor.departamento)

            binding.tvResultado.text = "Profesor encontrado:\n\n${profesor.toString()}"
            Toast.makeText(this, "Profesor cargado", Toast.LENGTH_SHORT).show()
        } else {
            binding.tvResultado.text = "No se encontró profesor con ID: $id"
        }
    }

    private fun limpiarCamposProfesor() {
        binding.etProfesorId.text?.clear()
        binding.etProfesorNombre.text?.clear()
        binding.etProfesorApellido.text?.clear()
        binding.etProfesorEdad.text?.clear()
        binding.etProfesorEspecialidad.text?.clear()
        binding.etProfesorDepartamento.text?.clear()
    }

    // ==================== FALLAS ====================

    private fun setupFallasListeners() {
        binding.btnFallaInsertar.setOnClickListener { insertarFalla() }
        binding.btnFallaActualizar.setOnClickListener { actualizarFalla() }
        binding.btnFallaEliminar.setOnClickListener { eliminarFalla() }
        binding.btnFallaListar.setOnClickListener { listarFallas() }
        binding.btnFallaBuscar.setOnClickListener { buscarFalla() }
    }

    private fun insertarFalla() {
        if (alumnoSeleccionadoId == 0) {
            Toast.makeText(this, "Selecciona un alumno", Toast.LENGTH_SHORT).show()
            return
        }

        val fecha = binding.etFallaFecha.text.toString()
        val descripcion = binding.etFallaMateria.text.toString()
        val tipoFalla = binding.spinnerTipoFalla.selectedItem?.toString() ?: ""

        if (fecha.isEmpty() || descripcion.isEmpty() || tipoFalla.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val alumnoSeleccionado = alumnosList.find { it.id == alumnoSeleccionadoId }
        if (alumnoSeleccionado == null) {
            Toast.makeText(this, "Error: Alumno no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        val falla = Falla(
            alumnoId = alumnoSeleccionadoId,
            nombreAlumno = alumnoSeleccionado.nombreCompleto(),
            fecha = fecha,
            materia = descripcion,
            tipo = tipoFalla
        )

        val id = fallaDAO.insertar(falla)
        if (id > 0) {
            Toast.makeText(this, "Falla registrada con ID: $id", Toast.LENGTH_SHORT).show()
            limpiarCamposFalla()
        } else {
            Toast.makeText(this, "Error al insertar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarFalla() {
        val idStr = binding.etFallaId.text.toString()

        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingresa el ID de la falla", Toast.LENGTH_SHORT).show()
            return
        }

        if (alumnoSeleccionadoId == 0) {
            Toast.makeText(this, "Selecciona un alumno", Toast.LENGTH_SHORT).show()
            return
        }

        val fecha = binding.etFallaFecha.text.toString()
        val descripcion = binding.etFallaMateria.text.toString()
        val tipoFalla = binding.spinnerTipoFalla.selectedItem?.toString() ?: ""

        if (fecha.isEmpty() || descripcion.isEmpty() || tipoFalla.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val id = idStr.toIntOrNull()
        if (id == null) {
            Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val alumnoSeleccionado = alumnosList.find { it.id == alumnoSeleccionadoId }
        if (alumnoSeleccionado == null) {
            Toast.makeText(this, "Error: Alumno no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        val falla = Falla(id, alumnoSeleccionadoId, alumnoSeleccionado.nombreCompleto(), fecha, descripcion, tipoFalla)
        val rowsAffected = fallaDAO.actualizar(falla)

        if (rowsAffected > 0) {
            Toast.makeText(this, "Falla actualizada", Toast.LENGTH_SHORT).show()
            limpiarCamposFalla()
        } else {
            Toast.makeText(this, "No se encontró la falla", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarFalla() {
        val idStr = binding.etFallaId.text.toString()

        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingresa el ID", Toast.LENGTH_SHORT).show()
            return
        }

        val id = idStr.toIntOrNull()
        if (id == null) {
            Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val rowsAffected = fallaDAO.eliminar(id)
        if (rowsAffected > 0) {
            Toast.makeText(this, "Falla eliminada", Toast.LENGTH_SHORT).show()
            limpiarCamposFalla()
        } else {
            Toast.makeText(this, "No se encontró la falla", Toast.LENGTH_SHORT).show()
        }
    }

    private fun listarFallas() {
        val fallas = fallaDAO.obtenerTodos()

        if (fallas.isEmpty()) {
            binding.tvResultado.text = "No hay fallas registradas"
        } else {
            val resultado = StringBuilder("=== REPORTE DE FALLAS (${fallas.size}) ===\n\n")
            fallas.forEach { falla ->
                resultado.append("ID Falla: ${falla.id}\n")
                resultado.append("Alumno: ${falla.nombreAlumno}\n")
                resultado.append("Fecha: ${falla.fecha}\n")
                resultado.append("Problema: ${falla.tipo}\n")
                resultado.append("Descripción: ${falla.materia}\n")
                resultado.append("------------------------\n")
            }
            binding.tvResultado.text = resultado.toString()
        }
    }

    private fun buscarFalla() {
        val idStr = binding.etFallaId.text.toString()

        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingresa el ID", Toast.LENGTH_SHORT).show()
            return
        }

        val id = idStr.toIntOrNull()
        if (id == null) {
            Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val falla = fallaDAO.obtenerPorId(id)

        if (falla != null) {
            // Buscar el índice del alumno en la lista
            val alumnoIndex = alumnosList.indexOfFirst { it.id == falla.alumnoId }
            if (alumnoIndex != -1) {
                binding.spinnerAlumno.setSelection(alumnoIndex)
            }

            binding.etFallaFecha.setText(falla.fecha)
            binding.etFallaMateria.setText(falla.materia)

            // Buscar el índice del tipo de falla en el spinner
            val tipoFallaAdapter = binding.spinnerTipoFalla.adapter
            for (i in 0 until tipoFallaAdapter.count) {
                if (tipoFallaAdapter.getItem(i).toString() == falla.tipo) {
                    binding.spinnerTipoFalla.setSelection(i)
                    break
                }
            }

            binding.tvResultado.text = "Falla encontrada:\n\n${falla.toString()}"
            Toast.makeText(this, "Falla cargada", Toast.LENGTH_SHORT).show()
        } else {
            binding.tvResultado.text = "No se encontró falla con ID: $id"
        }
    }

    private fun limpiarCamposFalla() {
        binding.etFallaId.text?.clear()
        binding.etFallaMateria.text?.clear()
        establecerFechaActual()

        // Resetear spinners a la primera opción
        if (binding.spinnerAlumno.adapter != null && binding.spinnerAlumno.adapter.count > 0) {
            binding.spinnerAlumno.setSelection(0)
        }
        if (binding.spinnerTipoFalla.adapter != null && binding.spinnerTipoFalla.adapter.count > 0) {
            binding.spinnerTipoFalla.setSelection(0)
        }
    }
}
