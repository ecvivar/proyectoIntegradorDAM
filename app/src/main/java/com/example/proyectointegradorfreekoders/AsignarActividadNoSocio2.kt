package com.example.proyectointegradorfreekoders

import android.os.Bundle
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectointegradorfreekoders.database.DBHelper
import com.google.android.material.button.MaterialButton
import java.util.ArrayList

class AsignarActividadNoSocio2 : AppCompatActivity() {

    private lateinit var db: DBHelper

    // Variables para pasar al formulario de pago
    private var noSocioId: Int = -1
    private var montoAPagar: Double = 0.0
    private var concepto: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asignar_actividad_no_socio2)
        // Inicializar DB
        db = DBHelper(this)

        // Datos del intent
        noSocioId = intent.getIntExtra("NO_SOCIO_ID", -1)
        montoAPagar = intent.getDoubleExtra("MONTO_A_PAGAR", 0.0)
        concepto = intent.getStringExtra("CONCEPTO") ?: "Error"
        val horarios = intent.getStringArrayListExtra("HORARIOS") ?: ArrayList()


        if (noSocioId == -1) {
            Toast.makeText(this, "Error: No se recibió el ID del No Socio.", Toast.LENGTH_LONG).show()
            finish()
            return
        }


        val noSocio = db.obtenerNoSocioPorId(noSocioId)

        val tvNombre = findViewById<TextView>(R.id.tvNombreNoSocio)
        val tvDni = findViewById<TextView>(R.id.tvDocumento)
        val tvActividad = findViewById<TextView>(R.id.tvActividad)
        val tvHorario = findViewById<TextView>(R.id.tvFechaHora)
        val tvMonto = findViewById<TextView>(R.id.tvPrecio)

        if (noSocio != null) {
            tvNombre.text = "${noSocio.nombre.uppercase()} ${noSocio.apellido.uppercase()}"
            tvDni.text = "DOCUMENTO: ${noSocio.dni}"
        } else {
            tvNombre.text = "NO SOCIO NO ENCONTRADO"
            tvDni.text = "DOCUMENTO: ---"
        }

        tvActividad.text = concepto
        tvHorario.text = horarios.joinToString("\n") // Muestra todos los horarios
        tvMonto.text = "$${String.format("%.2f", montoAPagar)}" // Formateado como dinero

        // Configurar botones
        val botonAtras = findViewById<ImageView>(R.id.btnAtras)
        botonAtras.setOnClickListener {
            finish()
        }

        val botonSiguiente = findViewById<MaterialButton>(R.id.btnSiguiente)
        botonSiguiente.setOnClickListener {
            // Pasar los datos al formulario de pago
            val intent = Intent(this, FormularioPagoActivity::class.java).apply {
                putExtra("CLIENTE_ID", noSocioId)
                putExtra("TIPO_CLIENTE", "no_socio")
                putExtra("CONCEPTO", concepto)
                putExtra("MONTO_A_PAGAR", montoAPagar)
            }
            startActivity(intent)
        }
    }
}