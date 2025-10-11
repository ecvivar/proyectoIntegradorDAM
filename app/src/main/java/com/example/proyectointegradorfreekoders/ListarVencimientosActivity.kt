package com.example.proyectointegradorfreekoders

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ListarVencimientosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Carga el layout que hemos diseñado
        setContentView(R.layout.activity_listarvencimientos)

        // botón volver
        val botonVolver = findViewById<MaterialButton>(R.id.btnVolver) // Corregido el ID
        botonVolver.setOnClickListener {
            finish()
        }

        //  botón IMPRIMIR PDF
        val botonImprimir = findViewById<Button>(R.id.imprimir)
        botonImprimir.setOnClickListener {
            Toast.makeText(this, "Imprimiendo la lista de vencimientos", Toast.LENGTH_SHORT).show()
        }
    }
}
