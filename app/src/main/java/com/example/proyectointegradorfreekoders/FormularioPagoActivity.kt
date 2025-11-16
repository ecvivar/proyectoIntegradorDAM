package com.example.proyectointegradorfreekoders

import com.example.proyectointegradorfreekoders.database.DBHelper
import com.example.proyectointegradorfreekoders.database.Pago
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import android.widget.TextView
import android.widget.Toast

class FormularioPagoActivity : AppCompatActivity() {
    private lateinit var spinnerCuotas: Spinner
    private lateinit var radioEfectivo: RadioButton
    private lateinit var radioTarjeta: RadioButton

    private lateinit var db: DBHelper
    private var clienteId: Int = -1
    private var tipoCliente: String = ""
    private var concepto: String = ""
    private var montoAPagar: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulariopago)

        //Inicializar DBHelper
        db = DBHelper(this)

        //Recibir datos del Intent
        clienteId = intent.getIntExtra("CLIENTE_ID", -1)
        tipoCliente = intent.getStringExtra("TIPO_CLIENTE") ?: ""
        concepto = intent.getStringExtra("CONCEPTO") ?: "Error"
        montoAPagar = intent.getDoubleExtra("MONTO_A_PAGAR", 0.0)

        //Validar datos recibidos
        if (clienteId == -1 || tipoCliente.isEmpty()) {
            Toast.makeText(this, "Error: Datos del cliente no recibidos.", Toast.LENGTH_LONG).show()
            finish()
            return // Detiene la ejecución de onCreate si los datos son inválidos
        }

        //Mostrar el monto en la pantalla
        val tvMonto = findViewById<TextView>(R.id.valorprecio)
        tvMonto.text = "$${String.format("%.2f", montoAPagar)}"

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
            //Obtener la fecha actual y el medio de pago
            val fechaDePago = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val medioDePago = if (radioEfectivo.isChecked) "Efectivo" else "Tarjeta"

            //Crear el objeto Pago con todos los datos
            val nuevoPago = Pago(
                id = 0, // El ID es autoincremental en la BD
                tipoPersona = tipoCliente, // "socio" o "no_socio"
                idReferencia = clienteId, // El ID de la persona que paga
                concepto = concepto, // "Cuota mensual" o "Acrobacias Aéreas, Fútbol", etc.
                monto = montoAPagar,
                fechaPago = fechaDePago,
                fechaVencimiento = "", // Para este pago, la fecha de vencimiento no aplica.
                medioPago = medioDePago
            )

            //Insertar en la base de datos
            val resultado = db.insertarPago(nuevoPago)

            //Verificar si el guardado fue exitoso
            if (resultado != -1L) {
                // ÉXITO: El pago se guardó correctamente.
                var mensaje = "Pago realizado con éxito:\n\nMonto: $${String.format("%.2f", montoAPagar)}\nConcepto: $concepto\nForma de pago: $medioDePago"

                if (medioDePago == "Tarjeta") {
                    val cuotasSeleccionadas = spinnerCuotas.selectedItem.toString()
                    mensaje += "\nCuotas: $cuotasSeleccionadas"
                }

                val intent = Intent(this, PagoCorrectoActivity::class.java)
                intent.putExtra("MENSAJE_PAGO", mensaje)
                startActivity(intent)
                finish() // Cierra esta pantalla para que el usuario no pueda volver con el botón "atrás"

            } else {
                // ERROR: El pago no se pudo guardar.
                Toast.makeText(this, "Error al registrar el pago en la base de datos", Toast.LENGTH_LONG).show()
            }

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
