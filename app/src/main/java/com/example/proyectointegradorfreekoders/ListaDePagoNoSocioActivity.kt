package com.example.proyectointegradorfreekoders

import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectointegradorfreekoders.database.DBHelper
import com.example.proyectointegradorfreekoders.database.Pago
import com.google.android.material.button.MaterialButton

// ADAPTADOR CORREGIDO PARA USAR EL LAYOUT PERSONALIZADO
class HistorialPagosAdapter(private val pagos: List<Pago>) : RecyclerView.Adapter<HistorialPagosAdapter.PagoViewHolder>() {

    // El ViewHolder ahora busca los IDs de 'list_item_historial_pago.xml'
    class PagoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvConcepto: TextView = view.findViewById(R.id.tvHistorialConcepto)
        val tvMonto: TextView = view.findViewById(R.id.tvHistorialMonto)
        val tvFecha: TextView = view.findViewById(R.id.tvHistorialFecha)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): PagoViewHolder {
        // Inflamos el layout de la fila con las tres columnas que diseñamos
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_historial_pago, parent, false)
        return PagoViewHolder(view)
    }

    override fun getItemCount() = pagos.size

    // Asignamos los datos del pago a los TextViews correctos
    override fun onBindViewHolder(holder: PagoViewHolder, position: Int) {
        val pago = pagos[position]
        holder.tvConcepto.text = pago.concepto
        holder.tvMonto.text = "$${String.format("%.2f", pago.monto)}"
        holder.tvFecha.text = pago.fechaPago
    }
}


class ListaDePagoNoSocioActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var rvHistorialPagos: RecyclerView
    private lateinit var tvNombrePersona: TextView
    private lateinit var tvNoHayPagos: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_pagos)

        db = DBHelper(this)

        rvHistorialPagos = findViewById(R.id.rvHistorialPagos)
        tvNombrePersona = findViewById(R.id.tvNombrePersona)
        tvNoHayPagos = findViewById(R.id.tvNoHayPagos)
        val botonVolver = findViewById<MaterialButton>(R.id.btnVolverHistorial)

        botonVolver.setOnClickListener { finish() }

        // --- LÓGICA PRINCIPAL (CORREGIDA Y VERIFICADA) ---

        // 1. Recibir los datos con la clave correcta "NO_SOCIO_ID"
        val noSocioId = intent.getIntExtra("NO_SOCIO_ID", -1)

        if (noSocioId == -1) {
            Toast.makeText(this, "Error: No se recibió la información del No Socio.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // 2. Cargar los datos y el historial de pagos
        cargarHistorial(noSocioId)
    }

    private fun cargarHistorial(id: Int) {
        val noSocio = db.obtenerNoSocioPorId(id)
        if (noSocio == null) {
            Toast.makeText(this, "Error: No se encontró a la persona en la base de datos.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        tvNombrePersona.text = "Historial de Pagos de:\n${noSocio.nombre} ${noSocio.apellido}"

        // Obtenemos el historial de pagos
        val cursor = db.getPagosNoSocioPorDNI(noSocio.dni)
        val listaDePagos = convertirCursorALista(cursor)

        // 3. Mostrar los datos en el RecyclerView
        if (listaDePagos.isEmpty()) {
            rvHistorialPagos.visibility = View.GONE
            tvNoHayPagos.visibility = View.VISIBLE
        } else {
            rvHistorialPagos.visibility = View.VISIBLE
            tvNoHayPagos.visibility = View.GONE

            rvHistorialPagos.layoutManager = LinearLayoutManager(this)
            // USAMOS EL NUEVO ADAPTADOR: HistorialPagosAdapter
            rvHistorialPagos.adapter = HistorialPagosAdapter(listaDePagos)
        }
    }

    // FUNCIÓN DE CONVERSIÓN CORREGIDA PARA COINCIDIR CON LA CONSULTA
    private fun convertirCursorALista(cursor: Cursor): List<Pago> {
        val lista = mutableListOf<Pago>()
        cursor.use {
            if (it.moveToFirst()) {
                // Obtenemos los índices de las columnas que SÍ existen en la consulta
                val conceptoIndex = it.getColumnIndexOrThrow("concepto")
                val montoIndex = it.getColumnIndexOrThrow("monto")
                val fechaPagoIndex = it.getColumnIndexOrThrow("fecha_pago")

                do {
                    // Creamos un objeto Pago solo con los datos que tenemos
                    val pago = Pago(
                        id = 0, // No lo necesitamos para mostrar
                        tipoPersona = "", // No lo necesitamos para mostrar
                        idReferencia = 0, // No lo necesitamos para mostrar
                        concepto = it.getString(conceptoIndex),
                        monto = it.getDouble(montoIndex),
                        fechaPago = it.getString(fechaPagoIndex),
                        medioPago = "" // No lo necesitamos para mostrar
                    )
                    lista.add(pago)
                } while (it.moveToNext())
            }
        }
        return lista
    }
}
