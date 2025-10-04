package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

    }
}