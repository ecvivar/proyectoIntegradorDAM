package com.example.proyectointegradorfreekoders

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ListaDePagoNoSocioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listadepagonosocio)

        val botonVolver = findViewById<MaterialButton>(R.id.btnVolver)
        botonVolver.setOnClickListener {
            finish()
        }

    }
}
