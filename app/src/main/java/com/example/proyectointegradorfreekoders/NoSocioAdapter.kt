package com.example.proyectointegradorfreekoders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectointegradorfreekoders.database.NoSocio


class NoSocioAdapter(
    private var lista: MutableList<NoSocio>,
    private val onNoSocioClick: (NoSocio) -> Unit
) : RecyclerView.Adapter<NoSocioAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreSocio)
        val tvDni: TextView = itemView.findViewById(R.id.tvDniSocio)
        val btnAccion: Button = itemView.findViewById(R.id.btnGenerarCarnet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_socio, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noSocio = lista[position]

        holder.tvNombre.text = "${noSocio.apellido}, ${noSocio.nombre}"
        holder.tvDni.text = "DNI: ${noSocio.dni}"
        holder.btnAccion.text = "Asignar"

        holder.btnAccion.setOnClickListener {
            onNoSocioClick(noSocio)
        }
    }

    fun updateList(nuevaLista: List<NoSocio>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}