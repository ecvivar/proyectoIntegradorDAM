package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectointegradorfreekoders.databinding.ActivityListadepagosocioBinding
import com.example.proyectointegradorfreekoders.database.DBHelper
import com.example.proyectointegradorfreekoders.CuotaAdapter

class ListaDePagoSocioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListadepagosocioBinding
    private lateinit var db: DBHelper

    private var socioId = 0
    private var nombreSocio = ""
    private var dniSocio = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListadepagosocioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)

        // Recibir datos del socio
        socioId = intent.getIntExtra("ID_CLIENTE", 0)
        nombreSocio = intent.getStringExtra("NOMBRE_CLIENTE") ?: ""
        dniSocio = intent.getStringExtra("DNI_CLIENTE") ?: ""

        binding.txtNombreSocio.text = "$nombreSocio - DNI $dniSocio"

        // Botón volver
        binding.btnVolver.setOnClickListener { finish() }

        // Botón siguiente
        binding.btnSiguiente.setOnClickListener {
            val intent = Intent(this, FormularioPagoActivity::class.java)
            intent.putExtra("socioId", socioId)
            startActivity(intent)
        }

        // Cargar cuotas
        val cuotas = db.obtenerCuotasSocio(socioId)
        binding.rvCuotas.layoutManager = LinearLayoutManager(this)
        binding.rvCuotas.adapter = CuotaAdapter(this, cuotas) { cuota, checked ->
            if (checked) db.marcarPagada(cuota.id)
        }
    }
}