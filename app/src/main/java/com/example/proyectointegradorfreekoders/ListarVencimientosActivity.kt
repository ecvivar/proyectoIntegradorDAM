package com.example.proyectointegradorfreekoders

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectointegradorfreekoders.database.DBHelper
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListarVencimientosActivity : AppCompatActivity() {

    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listarvencimientos)

        // Inicializar el DBHelper para poder usar la base de datos
        db = DBHelper(this)

        // Obtener la fecha de hoy en el formato que usa la base de datos (ej: "2025-11-15")
        val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Llamar a la función que busca en la BD y muestra los datos en pantalla
        cargarVencimientosDelDia(fechaHoy)


        // botón volver
        val botonVolver = findViewById<MaterialButton>(R.id.btnVolver)
        botonVolver.setOnClickListener {
            finish()
        }

        //  botón IMPRIMIR PDF
        val botonImprimir = findViewById<Button>(R.id.imprimir)
        botonImprimir.setOnClickListener {
            Toast.makeText(this, "Imprimiendo la lista de vencimientos", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Busca en la base de datos los socios con vencimiento en la fecha dada
     * y los muestra en el layout dinámicamente.
     */
    private fun cargarVencimientosDelDia(fechaHoy: String) {
        // Encontrar el LinearLayout que preparamos en el XML
        val layoutLista = findViewById<LinearLayout>(R.id.layout_lista_vencimientos)
        // Limpiar la lista por si tuviera algo de antes
        layoutLista.removeAllViews()

        // Llamar a la función del DBHelper que hace la consulta a la BD
        val cursor = db.getVencimientosDelDia(fechaHoy)

        // Verificar si la consulta no devolvió ningún socio
        if (cursor.count == 0) {
            // Si no hay vencimientos, mostrar un mensaje informativo
            val tvMensaje = TextView(this)
            tvMensaje.text = "No hay vencimientos para el día de hoy."
            tvMensaje.textSize = 18f
            tvMensaje.setPadding(16, 16, 16, 16)
            layoutLista.addView(tvMensaje)
            cursor.close() // ¡Importante cerrar el cursor!
            return
        }

        // Si la consulta SÍ devolvió socios, los recorremos uno por uno
        cursor.use { // 'use' se encarga de cerrar el cursor al finalizar
            while (it.moveToNext()) {
                // Para cada socio, obtenemos sus datos
                val nombre = it.getString(it.getColumnIndexOrThrow("nombre"))
                val apellido = it.getString(it.getColumnIndexOrThrow("apellido"))
                val dni = it.getString(it.getColumnIndexOrThrow("dni"))
                // val tipoPlan = it.getString(it.getColumnIndexOrThrow("tipo_plan"))

                val tvSocio = TextView(this)
                val inicial = nombre.firstOrNull()?.uppercaseChar() ?: '?'
                tvSocio.text = "• ${apellido.uppercase()}, ${nombre.uppercase()} (DNI: $dni)"
                tvSocio.textSize = 18f
                tvSocio.setPadding(8, 16, 8, 16)
                // Añadir la vista del socio al LinearLayout
                layoutLista.addView(tvSocio)

                // Añadir una línea divisoria
                val separador = TextView(this)
                separador.height = 1
                separador.setBackgroundColor(getColor(R.color.verde_oscuro))
                layoutLista.addView(separador)
            }
        }
    }
}
