package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class FormularioPagoActivity : AppCompatActivity() {
    private lateinit var spinnerCuotas: Spinner
    private lateinit var radioEfectivo: RadioButton
    private lateinit var radioTarjeta: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulariopago)

        val botonVolver = findViewById<MaterialButton>(R.id.btnVolverFormulario)
        val botonPagar = findViewById<MaterialButton>(R.id.btn_pagar)
        spinnerCuotas = findViewById(R.id.sp_cuotas)
        radioEfectivo = findViewById(R.id.rb_efectivo)
        radioTarjeta = findViewById(R.id.rb_tarjeta_credito)

        // botón para volver atrás
        botonVolver.setOnClickListener {
            finish()
        }

        val opcionesCuotas = resources.getStringArray(R.array.cuotas_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesCuotas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCuotas.adapter = adapter
        actualizarEstadoSpinner()
        radioEfectivo.setOnCheckedChangeListener { _, _ -> actualizarEstadoSpinner() }
        radioTarjeta.setOnCheckedChangeListener { _, _ -> actualizarEstadoSpinner() }

        //  botón Pagar
        botonPagar.setOnClickListener {
            val monto = findViewById<android.widget.TextView>(R.id.valorprecio).text.toString()
            val formaDePago = if (radioEfectivo.isChecked) "Efectivo" else "Tarjeta de crédito"
            var mensaje = "Pago realizado con éxito:\n\nMonto: $monto\nForma de pago: $formaDePago"

            if (formaDePago == "Tarjeta de crédito") {
                val cuotasSeleccionadas = spinnerCuotas.selectedItem.toString()
                mensaje += "\nCuotas: $cuotasSeleccionadas"
            }

            val intent = Intent(this, PagoCorrectoActivity::class.java)
            intent.putExtra("MENSAJE_PAGO", mensaje)
            startActivity(intent)
            finish()
        }
    }

    private fun actualizarEstadoSpinner() {
        if (radioEfectivo.isChecked) {
            spinnerCuotas.isEnabled = false
            spinnerCuotas.alpha = 0.5f
        } else {
            spinnerCuotas.isEnabled = true
            spinnerCuotas.alpha = 1.0f
        }
    }
}
