package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ListaDePagoNoSocioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listadepagonosocio)

        val flechahaciaAtras = findViewById<ImageView>(R.id.iv_back)
        flechahaciaAtras.setOnClickListener {
            finish()
        }

    }
}
