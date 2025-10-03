package com.example.proyectointegradorfreekoders

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.content.Intent

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

    }
}