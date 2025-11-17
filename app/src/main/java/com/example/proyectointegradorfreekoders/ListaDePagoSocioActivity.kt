package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectointegradorfreekoders.adapters.CuotaAdapter
import com.example.proyectointegradorfreekoders.database.Cuotas
import com.example.proyectointegradorfreekoders.database.DBHelper
import com.example.proyectointegradorfreekoders.databinding.ActivityListadepagosocioBinding
import java.io.Serializable

class ListaDePagoSocioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListadepagosocioBinding
    private lateinit var db: DBHelper
    private lateinit var adapter: CuotaAdapter

    private var socioId = -1
    private var nombreSocio = ""
    private var dniSocio = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListadepagosocioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)

        recibirDatos()
        if (socioId == -1) {
            finish()
            return
        }

        configurarVistas()
        setupRecyclerView()
        configurarListeners()
        cargarCuotas()
    }

    override fun onResume() {
        super.onResume()
        if (::db.isInitialized) {
            cargarCuotas()
        }
    }

    private fun recibirDatos() {
        // Corregimos la clave para que coincida con la que envía ListaPagoInicioActivity
        socioId = intent.getIntExtra("SOCIO_ID", -1)

        // Si el ID es válido, obtenemos los datos frescos desde la base de datos
        // para evitar inconsistencias o datos desactualizados.
        if (socioId != -1) {
            val socio = db.obtenerSocioPorId(socioId)
            if (socio != null) {
                nombreSocio = "${socio.nombre} ${socio.apellido}"
                dniSocio = socio.dni
            } else {
                // Si el ID es inválido, mostramos un error y preparamos para cerrar.
                Toast.makeText(this, "Error: Socio no encontrado en la base de datos.", Toast.LENGTH_LONG).show()
                socioId = -1 // Forzamos a -1 para que la lógica de onCreate cierre la actividad
            }
        }
    }


    private fun configurarVistas() {
        binding.txtNombreSocio.text = "$nombreSocio - DNI: $dniSocio"
    }

    private fun setupRecyclerView() {
        // Inicializa el adaptador con una lista vacía.
        // La lógica de clic del checkbox ahora está dentro del propio adaptador.
        adapter = CuotaAdapter(mutableListOf())
        binding.rvCuotas.layoutManager = LinearLayoutManager(this)
        binding.rvCuotas.adapter = adapter
    }

    private fun configurarListeners() {
        // Configura el botón para volver a la pantalla anterior
        binding.btnVolver.setOnClickListener { finish() }

        // --- ¡LÓGICA DEL BOTÓN SIGUIENTE! ---
        binding.btnSiguiente.setOnClickListener {
            // 1. Obtener la lista de cuotas seleccionadas desde el adaptador
            val cuotasSeleccionadas = adapter.obtenerCuotasSeleccionadas()

            // 2. Validar si se seleccionó al menos una cuota
            if (cuotasSeleccionadas.isEmpty()) {
                Toast.makeText(this, "Por favor, seleccione al menos una cuota para pagar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. Calcular el monto total y construir el concepto
            var montoTotal = 0.0
            val conceptos = mutableListOf<String>()
            val idsCuotas = mutableListOf<Int>()

            cuotasSeleccionadas.forEach { cuota ->
                montoTotal += obtenerMontoPlan(socioId) // Suma el monto de cada cuota
                conceptos.add("${cuota.mes}/${cuota.anio}") // Añade "mes/año" a la lista de conceptos
                idsCuotas.add(cuota.id) // Guarda el ID de la cuota
            }

            // 4. Crear el intent para ir al formulario de pago
            val intent = Intent(this, FormularioPagoActivity::class.java).apply {
                putExtra("CLIENTE_ID", socioId)
                putExtra("TIPO_CLIENTE", "SOCIO")
                putExtra("CONCEPTO", "Pago Cuotas: ${conceptos.joinToString(", ")}")
                putExtra("MONTO_A_PAGAR", montoTotal)
                // Se pasan los IDs de las cuotas para marcarlas como pagadas después
                putExtra("IDS_CUOTAS", idsCuotas as Serializable)
            }
            startActivity(intent)
        }
    }

    private fun cargarCuotas() {
        val listaDeCuotas = db.obtenerCuotasSocio(socioId)
        adapter.updateList(listaDeCuotas.filter { it.pagado == 0 }) // Mostramos solo las no pagadas
    }

    private fun obtenerMontoPlan(socioId: Int): Double {
        val socio = db.obtenerSocioPorId(socioId)
        return when (socio?.tipoPlan) {
            "Premium" -> 20000.0
            "Intermedio" -> 15000.0
            "Básico" -> 10000.0
            else -> 0.0
        }
    }
}
