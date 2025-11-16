package com.example.proyectointegradorfreekoders


import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectointegradorfreekoders.NoSocioAdapter
import com.example.proyectointegradorfreekoders.database.DBHelper
import com.google.android.material.button.MaterialButton

class ListaDePagoNoSocioActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var adapter: NoSocioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Reutilizamos el layout de búsqueda (tiene RecyclerView y EditText)
        setContentView(R.layout.activity_buscar_socio_imprimir)

        db = DBHelper(this)

        val rv = findViewById<RecyclerView>(R.id.rvSocios)
        val txtBuscar = findViewById<EditText>(R.id.txtBuscar)
        val botonVolver = findViewById<MaterialButton>(R.id.btnVolver)

        botonVolver.setOnClickListener {
            finish()
        }

        // Configurar Adapter
        adapter = NoSocioAdapter(
            db.obtenerTodosLosNoSocios().toMutableList()
        ) { noSocio ->
            mostrarHistorialPagos(noSocio.dni, "${noSocio.nombre} ${noSocio.apellido}")
        }

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        // Filtro de búsqueda
        txtBuscar.addTextChangedListener { editable ->
            val filtro = editable?.toString().orEmpty()
            val resultados = db.buscarNoSocioPorDni(filtro)
            adapter.updateList(resultados)
        }
    }

    private fun mostrarHistorialPagos(dni: String, nombre: String) {
        val cursor = db.getPagosNoSocioPorDNI(dni)


        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pagos de $nombre")

        if (cursor.count == 0) {
            builder.setMessage("No se encontraron pagos registrados.")
        } else {
            val sb = StringBuilder()
            while (cursor.moveToNext()) {
                val concepto = cursor.getString(cursor.getColumnIndexOrThrow("concepto"))
                val monto = cursor.getDouble(cursor.getColumnIndexOrThrow("monto"))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha_pago"))
                sb.append("• $fecha - $concepto ($$monto)\n")
            }
            builder.setMessage(sb.toString())
        }
        cursor.close()

        builder.setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}
