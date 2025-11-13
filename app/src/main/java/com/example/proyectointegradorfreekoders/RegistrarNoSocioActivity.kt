package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import com.example.proyectointegradorfreekoders.database.DBHelper
import com.example.proyectointegradorfreekoders.database.NoSocio

class RegistrarNoSocioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_no_socio)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Inicializar la base de datos
        val db = DBHelper(this)

        // Obtener fecha actual
        val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Botón volver atrás
        val botonVolver = findViewById<MaterialButton>(R.id.btnRegistrarNoSocio)
        botonVolver.setOnClickListener {
            finish()
        }

        // Botón Registrar No Socio
        val btnFinal = findViewById<MaterialButton>(R.id.btnRegistrarNoSocioFinal)
        btnFinal.setOnClickListener {
            try {

                // Obtener datos de los campos del formulario
                val nombre = findViewById<EditText>(R.id.txtNombre).text.toString().trim()
                val apellido = findViewById<EditText>(R.id.txtApellido).text.toString().trim()
                val telefono = findViewById<EditText>(R.id.txtTelefono).text.toString().trim()
                val direccion = findViewById<EditText>(R.id.txtDireccion).text.toString().trim()
                val email = findViewById<EditText>(R.id.txtEmail).text.toString().trim()
                val dni = findViewById<EditText>(R.id.txtDocumento).text.toString().trim()
                val aptoFisico = findViewById<CheckBox>(R.id.cbAptoFisico).isChecked

                // Validaciones básicas
                if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || dni.isEmpty()) {
                    Toast.makeText(this, "Complete todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Crear objeto no socio
                val noSocio = NoSocio(
                    id = 0,
                    dni = dni,
                    nombre = nombre,
                    apellido = apellido,
                    telefono = telefono,
                    direccion = direccion,
                    email = email,
                    aptoFisico = aptoFisico,
                    fechaAlta = fechaActual
                )

                // Guardar en la base de datos
                val resultado = db.insertarNoSocio(noSocio)

                if (resultado != -1L) {
                    // Registro exitoso → ir a la pantalla de confirmación
                    val intent = Intent(this, RegistrarNoSocioCorrectoActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Error → solo mostrar mensaje
                    Toast.makeText(this, "Error al registrar no socio", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Ocurrió un error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}