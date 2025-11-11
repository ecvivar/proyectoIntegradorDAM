package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 1 Inicializar la base de datos
        val db = DBHelper(this)

        // 2 Crear usuario por defecto si no existe
        val usuarios = db.obtenerUsuarios()
        if (usuarios.isEmpty()) {
            db.insertarUsuario(
                Usuario(
                    id = 0,
                    nombreUsuario = "admin@freekoders.com",
                    password = "1234",
                    rol = "administrador"
                )
            )
        }

        // 3 Referencias a los elementos de la interfaz
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val txtUsuario = findViewById<TextView>(R.id.txtUsuario)
        val txtPassword = findViewById<TextView>(R.id.txtPassword)

        // 4 Evento de login
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