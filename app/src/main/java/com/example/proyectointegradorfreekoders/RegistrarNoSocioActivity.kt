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
import com.example.proyectointegradorfreekoders.database.DBHelper
import com.example.proyectointegradorfreekoders.database.NoSocio
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegistrarNoSocioActivity : AppCompatActivity() {

    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_no_socio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = DBHelper(this)

        // Buscamos
        val tilNombre = findViewById<TextInputLayout>(R.id.tilNombre)
        val tilApellido = findViewById<TextInputLayout>(R.id.tilApellido)
        val tilTelefono = findViewById<TextInputLayout>(R.id.tilTelefono)
        val tilDireccion = findViewById<TextInputLayout>(R.id.tilDireccion)
        val tilDNI = findViewById<TextInputLayout>(R.id.tilNumeroDocumento)
        val etNombre = tilNombre.editText
        val etApellido = tilApellido.editText
        val etTelefono = tilTelefono.editText
        val etDireccion = tilDireccion.editText
        val etDNI = tilDNI.editText
        val etEmail = findViewById<EditText>(R.id.txtEmail)
        val chkAptoFisico = findViewById<CheckBox>(R.id.cbAptoFisico)
        val btnFinal = findViewById<MaterialButton>(R.id.btnRegistrarNoSocioFinal)
        val botonVolver = findViewById<MaterialButton>(R.id.btnRegistrarNoSocio)


        botonVolver.setOnClickListener {
            finish()
        }

        btnFinal.setOnClickListener {
            try {
                // Recolectamos los datos
                val nombre = etNombre?.text.toString().trim()
                val apellido = etApellido?.text.toString().trim()
                val telefono = etTelefono?.text.toString().trim()
                val direccion = etDireccion?.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val dni = etDNI?.text.toString().trim()
                val aptoFisico = chkAptoFisico.isChecked

                // Validacion
                if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
                    Toast.makeText(this, "Nombre, Apellido y DNI son obligatorios", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Creamos el objeto NoSocio
                val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

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

                // Guardamos en la Base de Datos
                val resultado = db.insertarNoSocio(noSocio)

                // Verificamos el resultado
                if (resultado != -1L) {
                    val intent = Intent(this, RegistrarNoSocioCorrectoActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error al registrar. ¿El DNI ya existe?", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Ocurrió un error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}