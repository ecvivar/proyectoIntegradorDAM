package com.example.proyectointegradorfreekoders

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.content.Intent
import com.google.android.material.button.MaterialButton

class MenuPrincipal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)

        val btnMenu = findViewById<ImageView>(R.id.btnMenu)

        btnMenu.setOnClickListener {
            val intent = Intent(this, OtrasOperaciones::class.java)
            startActivity(intent)
        }

        // Botón Registrar Socio
        val botonRegistrarSocio = findViewById<MaterialButton>(R.id.btnRegistrarSocio)

        botonRegistrarSocio.setOnClickListener {
            val intent = Intent(this, RegistrarSocioActivity::class.java)
            startActivity(intent)
        }

        // Botón Registrar No Socio
        val botonRegistrarNoSocio = findViewById<MaterialButton>(R.id.btnRegistrarNoSocio)

        botonRegistrarNoSocio.setOnClickListener {
            val intent = Intent(this, RegistrarNoSocioActivity::class.java)
            startActivity(intent)
        }

    }
}