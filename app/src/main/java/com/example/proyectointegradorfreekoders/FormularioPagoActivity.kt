package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class FormularioPagoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulariopago)

        val botonVolver = findViewById<ImageView>(R.id.iv_back)
        botonVolver.setOnClickListener {
            finish()
        }

        val botonPagar = findViewById<MaterialButton>(R.id.btn_pagar)
        botonPagar.setOnClickListener {
            val intent = Intent(this, PagoCorrectoActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
