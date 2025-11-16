package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import com.example.proyectointegradorfreekoders.adapters.ListaPagoAdapter
import com.example.proyectointegradorfreekoders.database.DBHelper
import com.example.proyectointegradorfreekoders.database.Socio
import com.example.proyectointegradorfreekoders.database.NoSocio
import com.google.android.material.button.MaterialButton

class ListaDePagoInicioActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var buscador: EditText
    private lateinit var adapter: ListaPagoAdapter
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listadepagoinicio)

        db = DBHelper(this)

        val botonVolver = findViewById<MaterialButton>(R.id.btnVolver)
        botonVolver.setOnClickListener { finish() }

        recyclerView = findViewById(R.id.rvListaClientes)
        buscador = findViewById(R.id.edtBuscarDni)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializamos adaptador con callback
        adapter = ListaPagoAdapter(mutableListOf()) { cliente ->
            abrirDetalleCliente(cliente)
        }

        recyclerView.adapter = adapter

        cargarTodosLosClientes()

        buscador.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrEmpty()) cargarTodosLosClientes()
                else buscarClientesPorDni(text.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // Obtener todos los socios y no socios de la base de datos
    private fun cargarTodosLosClientes() {
        val socios = db.obtenerTodosLosSocios()
        val noSocios = db.obtenerTodosLosNoSocios()

        val listaClientes = combinarClientes(socios, noSocios)
        adapter.updateList(listaClientes)
    }

    // Busca clientes por DNI y combina los resultados
    private fun buscarClientesPorDni(dni: String) {
        val socios = db.buscarSocioPorDni(dni)
        val noSocios = db.buscarNoSocioPorDni(dni)

        val listaClientes = combinarClientes(socios, noSocios)
        adapter.updateList(listaClientes)
    }

    // Combina socios y no socios en una lista de clientes
    private fun combinarClientes(
        socios: List<Socio>,
        noSocios: List<NoSocio>
    ): List<ItemClientePago> {

        val lista = mutableListOf<ItemClientePago>()

        socios.forEach {
            lista.add(
                ItemClientePago(
                    id = it.id,
                    dni = it.dni,
                    nombreCompleto = "${it.nombre} ${it.apellido}",
                    tipoCliente = "SOCIO"
                )
            )
        }
        noSocios.forEach {
            lista.add(
                ItemClientePago(
                    id = it.id,
                    dni = it.dni,
                    nombreCompleto = "${it.nombre} ${it.apellido}",
                    tipoCliente = "NO_SOCIO"
                )
            )
        }
        return lista.sortedBy { it.nombreCompleto.uppercase() }
    }

    // Abre la actividad de detalle del cliente
    private fun abrirDetalleCliente(cliente: ItemClientePago) {
        val intent = Intent(this, ListaDePagoSocioActivity::class.java)
        intent.putExtra("ID_CLIENTE", cliente.id)
        intent.putExtra("DNI_CLIENTE", cliente.dni)
        intent.putExtra("NOMBRE_CLIENTE", cliente.nombreCompleto)
        intent.putExtra("TIPO_CLIENTE", cliente.tipoCliente)
        startActivity(intent)
    }
}