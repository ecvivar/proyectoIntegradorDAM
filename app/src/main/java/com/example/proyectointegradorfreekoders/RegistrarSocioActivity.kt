package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

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
        // Botón Registrar socio
        val botonVolver = findViewById<MaterialButton>(R.id.btnRegistrarSocio)
        botonVolver.setOnClickListener {
            finish()
        }

        val btnFinal = findViewById<MaterialButton>(R.id.btnRegistrarSocioFinal)

        btnFinal.setOnClickListener {
            // Lógica para guardar los datos del socio en la base de datos...
            // y una vez que la lógica es exitosa:
            // Navegamos a la pantalla de confirmación
            val intent = Intent(this, RegistrarSocioCorrectoActivity::class.java)
            startActivity(intent)
        }
    }
}