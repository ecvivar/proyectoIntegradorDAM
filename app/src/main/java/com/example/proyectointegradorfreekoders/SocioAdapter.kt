package com.example.proyectointegradorfreekoders.database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectointegradorfreekoders.R

class SocioAdapter(
    private var lista: MutableList<Socio>,
    private val onClickGenerarCarnet: (Socio) -> Unit
) : RecyclerView.Adapter<SocioAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreSocio)
        val tvDni: TextView = itemView.findViewById(R.id.tvDniSocio)
        val btnCarnet: Button = itemView.findViewById(R.id.btnGenerarCarnet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_socio, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val socio = lista[position]

        holder.tvNombre.text = "${socio.apellido}, ${socio.nombre}"
        holder.tvDni.text = "DNI: ${socio.dni}"

        holder.btnCarnet.setOnClickListener {
            onClickGenerarCarnet(socio)
        }
    }

    fun updateList(nuevaLista: List<Socio>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}