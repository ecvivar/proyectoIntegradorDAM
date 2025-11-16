package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import java.util.ArrayList

class AsignarActividadNoSocioActivity : AppCompatActivity() {

    // Variable para guardar el ID del No Socio que recibimos
    private var noSocioId: Int = -1
    // Listas para mapear los checkboxes con sus datos
    private lateinit var allCheckBoxes: List<Pair<CheckBox, Double>>
    private lateinit var allActivityNames: Map<CheckBox, String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_asignar_actividad_no_socio)

        noSocioId = intent.getIntExtra("NO_SOCIO_ID", -1)

        if (noSocioId == -1) {
            Toast.makeText(this, "Error: No se seleccionó un No Socio.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonVolver = findViewById<MaterialButton>(R.id.btnActividades)
        botonVolver.setOnClickListener {
            finish()
        }

        setupExpandableCard(
            headerView = findViewById(R.id.encabezado_acrobacias),
            contentView = findViewById(R.id.contenido_acrobacias),
            arrowView = findViewById(R.id.flecha_acrobacias)
        )

        setupExpandableCard(
            headerView = findViewById(R.id.encabezado_futbol),
            contentView = findViewById(R.id.contenido_futbol),
            arrowView = findViewById(R.id.flecha_futbol)
        )

        setupExpandableCard(
            headerView = findViewById(R.id.encabezado_hockey),
            contentView = findViewById(R.id.contenido_hockey),
            arrowView = findViewById(R.id.flecha_hockey)
        )

        val acrobaciasPrice = 3800.0
        val futbolPrice = 3500.0
        val hockeyPrice = 3700.0

        allCheckBoxes = listOf(
            findViewById<CheckBox>(R.id.acrobacias_time1) to acrobaciasPrice,
            findViewById<CheckBox>(R.id.acrobacias_time2) to acrobaciasPrice,
            findViewById<CheckBox>(R.id.acrobacias_time3) to acrobaciasPrice,
            findViewById<CheckBox>(R.id.futbol_time1) to futbolPrice,
            findViewById<CheckBox>(R.id.futbol_time2) to futbolPrice,
            findViewById<CheckBox>(R.id.futbol_time3) to futbolPrice,
            findViewById<CheckBox>(R.id.hockey_time1) to hockeyPrice,
            findViewById<CheckBox>(R.id.hockey_time2) to hockeyPrice,
            findViewById<CheckBox>(R.id.hockey_time3) to hockeyPrice
        )

        allActivityNames = mapOf(
            findViewById<CheckBox>(R.id.acrobacias_time1) to "Acrobacias Aéreas",
            findViewById<CheckBox>(R.id.acrobacias_time2) to "Acrobacias Aéreas",
            findViewById<CheckBox>(R.id.acrobacias_time3) to "Acrobacias Aéreas",
            findViewById<CheckBox>(R.id.futbol_time1) to "Fútbol",
            findViewById<CheckBox>(R.id.futbol_time2) to "Fútbol",
            findViewById<CheckBox>(R.id.futbol_time3) to "Fútbol",
            findViewById<CheckBox>(R.id.hockey_time1) to "Hockey",
            findViewById<CheckBox>(R.id.hockey_time2) to "Hockey",
            findViewById<CheckBox>(R.id.hockey_time3) to "Hockey"
        )

        val btnFinal = findViewById<MaterialButton>(R.id.btnSiguiente)
        btnFinal.setOnClickListener {

            val seleccionados = allCheckBoxes.filter { it.first.isChecked }

            if (seleccionados.isEmpty()) {
                Toast.makeText(this, "Seleccioná al menos un horario", Toast.LENGTH_SHORT).show()
            } else {
                var montoTotal = 0.0
                val conceptos = ArrayList<String>()
                val horarios = ArrayList<String>()

                seleccionados.forEach { (checkbox, precio) ->
                    montoTotal += precio
                    val nombreActividad = allActivityNames[checkbox] ?: "Actividad"
                    if (!conceptos.contains(nombreActividad)) {
                        conceptos.add(nombreActividad)
                    }
                    horarios.add(checkbox.text.toString())
                }

                // Preparamos el intent
                val intent = Intent(this, AsignarActividadNoSocio2::class.java).apply {
                    putExtra("NO_SOCIO_ID", noSocioId) // <-- El ID que recibimos
                    putExtra("MONTO_A_PAGAR", montoTotal)
                    putExtra("CONCEPTO", conceptos.joinToString(", "))
                    putStringArrayListExtra("HORARIOS", horarios)
                }
                startActivity(intent)
            }
        }
    }

    private fun setupExpandableCard(headerView: View, contentView: View, arrowView: ImageView) {
        headerView.setOnClickListener {
            if (contentView.visibility == View.VISIBLE) {
                contentView.visibility = View.GONE
                arrowView.setImageResource(R.drawable.arrow_down)
            } else {
                contentView.visibility = View.VISIBLE
                arrowView.setImageResource(R.drawable.arrow_up)
            }
        }
    }
}