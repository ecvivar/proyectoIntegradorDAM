package com.example.proyectointegradorfreekoders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectointegradorfreekoders.R
import com.example.proyectointegradorfreekoders.database.NoSocio

/**
 * Este Adapter es una copia de tu SocioAdapter, pero para NoSocios.
 */
class NoSocioAdapter(
    private var lista: MutableList<NoSocio>,
    private val onNoSocioClick: (NoSocio) -> Unit // Lambda para manejar el clic
) : RecyclerView.Adapter<NoSocioAdapter.ViewHolder>() {

    // Reutilizamos el ViewHolder del SocioAdapter
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreSocio)
        val tvDni: TextView = itemView.findViewById(R.id.tvDniSocio)
        val btnAccion: Button = itemView.findViewById(R.id.btnGenerarCarnet) // Reutilizamos el ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Reutilizamos el layout 'item_socio.xml'
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_socio, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noSocio = lista[position]

        holder.tvNombre.text = "${noSocio.apellido}, ${noSocio.nombre}"
        holder.tvDni.text = "DNI: ${noSocio.dni}"
        holder.btnAccion.text = "Asignar" // Cambiamos el texto del botón

        holder.btnAccion.setOnClickListener {
            onNoSocioClick(noSocio) // Ejecuta la acción
        }
    }

    fun updateList(nuevaLista: List<NoSocio>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}