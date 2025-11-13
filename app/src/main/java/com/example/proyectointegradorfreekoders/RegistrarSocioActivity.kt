package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import android.widget.AutoCompleteTextView
import com.example.proyectointegradorfreekoders.database.DBHelper
import com.example.proyectointegradorfreekoders.database.Socio

class RegistrarSocioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_socio)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar la base de datos
        val db = DBHelper(this)

        // Botón volver atrás
        val botonVolver = findViewById<MaterialButton>(R.id.btnRegistrarSocio)
        botonVolver.setOnClickListener {
            finish()
        }

        // Spinner (AutoCompleteTextView) para Tipo de Plan
        val autoTipoPlan = findViewById<AutoCompleteTextView>(R.id.txtPlan)
        val planes = resources.getStringArray(R.array.tipoPlan)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, planes)
        autoTipoPlan.setAdapter(adapter)

        // Botón Registrar socio)
        val btnFinal = findViewById<MaterialButton>(R.id.btnRegistrarSocioFinal)

        btnFinal.setOnClickListener {
            try {

                // Obtener datos de los campos del formulario
                val nombre = findViewById<EditText>(R.id.txtNombre).text.toString().trim()
                val apellido = findViewById<EditText>(R.id.txtApellido).text.toString().trim()
                val telefono = findViewById<EditText>(R.id.txtTelefono).text.toString().trim()
                val direccion = findViewById<EditText>(R.id.txtDireccion).text.toString().trim()
                val email = findViewById<EditText>(R.id.txtEmail).text.toString().trim()
                val dni = findViewById<EditText>(R.id.txtDocumento).text.toString().trim()
                val tipoPlan = autoTipoPlan.text.toString().trim()
                val aptoFisico = findViewById<CheckBox>(R.id.cbAptoFisico).isChecked

                // Validaciones básicas
                if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || tipoPlan.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Complete todos los campos obligatorios",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                // Crear objeto socio
                val socio = Socio(
                    id = 0,
                    dni = dni,
                    nombre = nombre,
                    apellido = apellido,
                    telefono = telefono,
                    direccion = direccion,
                    email = email,
                    tipoPlan = tipoPlan,
                    aptoFisico = aptoFisico,
                    foto = null,
                    fechaAlta = "2025-11-12"
                )

                // Guardar en la base de datos
                val resultado = db.insertarSocio(socio)

                if (resultado != -1L) {
                    // Registro exitoso → ir a la pantalla de confirmación
                    val intent = Intent(this, RegistrarSocioCorrectoActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error al registrar socio", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Ocurrió un error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}