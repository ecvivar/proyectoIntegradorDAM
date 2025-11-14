package com.example.proyectointegradorfreekoders

import android.os.Bundle
import android.widget.EditText
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.widget.addTextChangedListener

import com.example.proyectointegradorfreekoders.database.DBHelper
import com.example.proyectointegradorfreekoders.database.Socio
import com.google.android.material.button.MaterialButton

class BuscarSocioImprimirActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var listaSocios: MutableList<Socio>
    private lateinit var rv: RecyclerView
    private lateinit var adapter: SocioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_socio_imprimir)

        db = DBHelper(this)
        rv = findViewById(R.id.rvSocios)

        // Botón volver atrás
        val btnVolver = findViewById<MaterialButton>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish()
        }

        cargarSocios()

        val txtBuscar = findViewById<EditText>(R.id.txtBuscar)

        // Buscar en vivo
        txtBuscar.addTextChangedListener {
            filtrarPorDni(txtBuscar.text.toString())
        }
    }

    private fun cargarSocios() {
        listaSocios = db.obtenerTodosLosSocios().toMutableList()

        adapter = SocioAdapter(listaSocios) { socioSeleccionado ->
            val intent = Intent(this, ImprimirCarnetSocio::class.java)
            intent.putExtra("idSocio", socioSeleccionado.id)
            startActivity(intent)
        }

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private fun filtrarPorDni(dni: String) {
        val filtrados = listaSocios.filter {
            it.dni.contains(dni)
        }
        rv.adapter = SocioAdapter(filtrados) { socioSeleccionado ->
            val intent = Intent(this, ImprimirCarnetSocio::class.java)
            intent.putExtra("idSocio", socioSeleccionado.id)
            startActivity(intent)
        }
    }
}