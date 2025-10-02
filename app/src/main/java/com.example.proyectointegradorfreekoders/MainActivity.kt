package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val txtUsuario = findViewById<TextView>(R.id.txtUsuario)
        val txtPassword = findViewById<TextView>(R.id.txtPassword)
        btnIngresar.setOnClickListener {
            val intent = Intent(this, MenuPrincipal::class.java)
            startActivity(intent)
        }
    }
}