package com.example.proyectointegradorfreekoders

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.view.LayoutInflater
import android.widget.Button

import com.example.proyectointegradorfreekoders.database.DBHelper
import com.example.proyectointegradorfreekoders.database.Socio
class SocioAdapter(
    private val lista: List<Socio>,
    private val onClickCarnet: (Socio) -> Unit
) : RecyclerView.Adapter<SocioAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre = itemView.findViewById<TextView>(R.id.tvNombreSocio)
        val dni = itemView.findViewById<TextView>(R.id.tvDniSocio)
        val btnCarnet = itemView.findViewById<Button>(R.id.btnGenerarCarnet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_socio, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val socio = lista[position]
        holder.nombre.text = "${socio.nombre} ${socio.apellido}"
        holder.dni.text = "DNI: ${socio.dni}"

        holder.btnCarnet.setOnClickListener {
            onClickCarnet(socio)
        }
    }
}