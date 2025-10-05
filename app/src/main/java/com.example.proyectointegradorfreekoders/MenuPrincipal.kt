package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout


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

//        val botonListaDePagos = findViewById<MaterialButton>(R.id.btnListaPagos)
//        botonListaDePagos.setOnClickListener {
//            val intent = Intent(this, ListaDePagoInicioActivity::class.java)
//            startActivity(intent)
//
//        }

    }
}