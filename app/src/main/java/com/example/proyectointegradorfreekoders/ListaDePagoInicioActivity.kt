package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ListaDePagoInicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listadepagoinicio)

        val flechaAtras = findViewById<ImageView>(R.id.iv_back)
        flechaAtras.setOnClickListener {
            finish()
        }

        val botonSocio = findViewById<Button>(R.id.btn_socio)
        botonSocio.setOnClickListener {
            val intent = Intent(this, ListaDePagoSocioActivity::class.java)
            startActivity(intent)
        }

        val botonNoSocio = findViewById<Button>(R.id.btn_no_socio)
        botonNoSocio.setOnClickListener {
            val intent = Intent(this, ListaDePagoNoSocioActivity::class.java)
            startActivity(intent)
        }
    }
}
