package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
<<<<<<< HEAD
import android.widget.ImageView
import android.content.Intent
import com.google.android.material.button.MaterialButton
=======
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

>>>>>>> dab7f21dc70037aed5f423eac5ad45a38f8d72ae

class MenuPrincipal : AppCompatActivity() {

    private lateinit var btnMenu: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)

        btnMenu = findViewById(R.id.btnMenu)

        btnMenu.setOnClickListener {
            val bottomSheet = MenuBottomSheet()
            bottomSheet.show(supportFragmentManager, "MenuBottomSheet")
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