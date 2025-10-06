package com.example.proyectointegradorfreekoders

import android.R.*
import android.os.Bundle
import android.content.Intent
import android.widget.ImageView
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class AsignarActividadNoSocio2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asignar_actividad_no_socio2)

//        val botonAtras = findViewById<MaterialButton>(R.id.btnAtras)
//        botonAtras.setOnClickListener {
//            finish()
//        }

        val botonSiguiente = findViewById<MaterialButton>(R.id.btnSiguiente)
        botonSiguiente.setOnClickListener {
            val intent = Intent(this, FormularioPagoActivity::class.java)
            startActivity(intent)
        }
    }
}