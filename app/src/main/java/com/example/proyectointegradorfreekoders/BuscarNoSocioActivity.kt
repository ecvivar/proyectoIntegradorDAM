package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectointegradorfreekoders.NoSocioAdapter
import com.example.proyectointegradorfreekoders.database.DBHelper
import com.google.android.material.button.MaterialButton

/**
 * Esta es la pantalla que FALTA. Es el paso intermedio para
 * SELECCIONAR un No Socio antes de asignarle una actividad.
 */
class BuscarNoSocioActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var adapter: NoSocioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Reutilizamos el layout de búsqueda de imprimir carnet
        setContentView(R.layout.activity_buscar_socio_imprimir)

        db = DBHelper(this)

        val rv = findViewById<RecyclerView>(R.id.rvSocios)
        val txtBuscar = findViewById<EditText>(R.id.txtBuscar)
        val botonVolver = findViewById<MaterialButton>(R.id.btnVolver)

        botonVolver.setOnClickListener {
            finish()
        }

        // 1. Configura el Adapter para NoSocios
        adapter = NoSocioAdapter(
            db.obtenerTodosLosNoSocios().toMutableList() // Usa la función del DBHelper
        ) { noSocio ->
            // 2. Al hacer clic, lanza AsignarActividad y pasa el ID
            val intent = Intent(this, AsignarActividadNoSocioActivity::class.java)
            intent.putExtra("NO_SOCIO_ID", noSocio.id)
            startActivity(intent)
        }

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        // 3. El filtro ahora usa la función de buscar No Socios
        txtBuscar.addTextChangedListener { editable ->
            val filtro = editable?.toString().orEmpty()
            val resultados = db.buscarNoSocioPorDni(filtro) // Usa la función del DBHelper
            adapter.updateList(resultados)
        }
    }
}