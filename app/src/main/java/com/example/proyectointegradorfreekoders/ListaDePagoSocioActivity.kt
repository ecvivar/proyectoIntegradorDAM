package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
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
        }
    }
}
