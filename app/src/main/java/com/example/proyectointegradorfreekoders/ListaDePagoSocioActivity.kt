package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
<<<<<<< HEAD
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
=======
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectointegradorfreekoders.adapters.SocioAdapter
import com.example.proyectointegradorfreekoders.database.DBHelper
import com.google.android.material.button.MaterialButton

class ListaDePagoSocioActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var adapter: SocioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_socio_imprimir)

        db = DBHelper(this)

        val rv = findViewById<RecyclerView>(R.id.rvSocios)
        val txtBuscar = findViewById<EditText>(R.id.txtBuscar)
        val botonVolver = findViewById<MaterialButton>(R.id.btnVolver)

        botonVolver.setOnClickListener {
            finish()
        }

        adapter = SocioAdapter(
            db.obtenerTodosLosSocios().toMutableList()
        ) { socio ->
            // cobrar cuota
            val intent = Intent(this, FormularioPagoActivity::class.java).apply {
                putExtra("CLIENTE_ID", socio.id)
                putExtra("TIPO_CLIENTE", "socio")
                putExtra("CONCEPTO", "Cuota Mensual - ${socio.tipoPlan}")
                putExtra("MONTO_A_PAGAR", 15000.0)
            }
            startActivity(intent)
        }

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        txtBuscar.addTextChangedListener { editable ->
            val filtro = editable?.toString().orEmpty()
            val resultados = db.buscarSocioPorDni(filtro)
            adapter.updateList(resultados)
>>>>>>> diego-e4
        }
    }
}