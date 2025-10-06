package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class AsignarActividadNoSocioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_asignar_actividad_no_socio)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botón para volver atrás
        val botonVolver = findViewById<MaterialButton>(R.id.btnActividades)
        botonVolver.setOnClickListener {
            finish()
        }

        // --- Lógica para tarjetas expandibles ---
        // Se configura cada tarjeta llamando a una función auxiliar para no repetir código.
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

        // --- Lógica del Botón SIGUIENTE ---

        // Se crea una lista con todos los checkboxes de todas las actividades.
        val allCheckBoxes = listOf<CheckBox>(
            findViewById(R.id.acrobacias_time1),
            findViewById(R.id.acrobacias_time2),
            findViewById(R.id.acrobacias_time3),
            findViewById(R.id.futbol_time1),
            findViewById(R.id.futbol_time2),
            findViewById(R.id.futbol_time3),
            findViewById(R.id.hockey_time1),
            findViewById(R.id.hockey_time2),
            findViewById(R.id.hockey_time3)
        )

        val btnFinal = findViewById<MaterialButton>(R.id.btnSiguiente)
        btnFinal.setOnClickListener {
            // Se filtran solo los checkboxes que están marcados y se obtiene su texto.
            val horariosSeleccionados = allCheckBoxes
                .filter { it.isChecked }
                .map { it.text.toString() }

            if (horariosSeleccionados.isEmpty()) {
                Toast.makeText(this, "Seleccioná al menos un horario", Toast.LENGTH_SHORT).show()
            } else {
                // Si hay horarios, se prepara el intent para la siguiente pantalla.
                val intent = Intent(this, RegistrarSocioCorrectoActivity::class.java).apply {
                    putStringArrayListExtra("horarios", ArrayList(horariosSeleccionados))
                }
                startActivity(intent)
            }
        }
    }

    /**
     * Función auxiliar que configura un encabezado para que muestre/oculte
     * un contenido y rote una flecha al ser presionado.
     *
     * @param headerView La vista del encabezado que recibe el clic.
     * @param contentView La vista del contenido que se mostrará u ocultará.
     * @param arrowView La ImageView de la flecha que rotará.
     */
    private fun setupExpandableCard(headerView: View, contentView: View, arrowView: ImageView) {
        headerView.setOnClickListener {
            if (contentView.visibility == View.VISIBLE) {
                // Si está visible, se oculta y la flecha apunta hacia abajo.
                contentView.visibility = View.GONE
                arrowView.setImageResource(R.drawable.arrow_down)
            } else {
                // Si está oculto, se muestra y la flecha apunta hacia arriba.
                contentView.visibility = View.VISIBLE
                arrowView.setImageResource(R.drawable.arrow_up)
            }
        }
    }
}
