package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.example.proyectointegradorfreekoders.database.DBHelper
import com.example.proyectointegradorfreekoders.database.Usuario

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inicializar la base de datos
        val db = DBHelper(this)

        // Acceder a las preferencias compartidas
        val prefs = getSharedPreferences("freekoders_prefs", MODE_PRIVATE)
        val usuarioCreado = prefs.getBoolean("usuario_inicial_creado", false)

        // Crear usuario admin solo si no existe
        if (!usuarioCreado) {
            val resultado = db.insertarUsuario(
                Usuario(
                    id = 0,
                    nombre_usuario = "admin@correo.com",
                    password = "1234",
                    rol = "administrador"
                )
            )
            if (resultado != -1L) {
                // Guardar la bandera para no volver a crear el usuario
                prefs.edit().putBoolean("usuario_inicial_creado", true).apply()
                Toast.makeText(this, "Usuario admin creado por primera vez", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, "Error al crear el usuario admin", Toast.LENGTH_LONG).show()
            }
        }

        // Referencias a los elementos de la interfaz
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val txtUsuario = findViewById<TextView>(R.id.txtUsuario)
        val txtPassword = findViewById<TextView>(R.id.txtPassword)

        // Evento de login
        btnIngresar.setOnClickListener {
            val usuario = txtUsuario.text.toString().trim()
            val password = txtPassword.text.toString().trim()

            if (db.validarUsuario(usuario, password)) {
                // Si es correcto, abrir el menú principal
                val intent = Intent(this, MenuPrincipal::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}