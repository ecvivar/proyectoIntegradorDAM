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
import com.example.proyectointegradorfreekoders.adapters.SocioAdapter

import com.example.proyectointegradorfreekoders.database.DBHelper
import com.example.proyectointegradorfreekoders.database.Socio
import com.google.android.material.button.MaterialButton

class BuscarSocioImprimirActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var adapter: SocioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_socio_imprimir)

        db = DBHelper(this)

        val rv = findViewById<RecyclerView>(R.id.rvSocios)
        val txtBuscar = findViewById<EditText>(R.id.txtBuscar)

        // Botón volver atrás
        val botonVolver = findViewById<MaterialButton>(R.id.btnVolver)
        botonVolver.setOnClickListener {
            finish()
        }

        adapter = SocioAdapter(
            db.obtenerTodosLosSocios().toMutableList()
        ) { socio ->
            val intent = Intent(this, ImprimirCarnetSocio::class.java)
            intent.putExtra("idSocio", socio.id)
            startActivity(intent)
        }

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        txtBuscar.addTextChangedListener { editable ->
            val filtro = editable?.toString().orEmpty()
            val resultados = db.buscarSocioPorDni(filtro)
            adapter.updateList(resultados)
        }
    }
}