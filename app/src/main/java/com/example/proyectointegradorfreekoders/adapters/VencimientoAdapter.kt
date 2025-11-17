package com.example.proyectointegradorfreekoders.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectointegradorfreekoders.R
import com.example.proyectointegradorfreekoders.database.SocioConDeuda // Crearemos esta clase

class VencimientoAdapter(private val socios: List<SocioConDeuda>) :
    RecyclerView.Adapter<VencimientoAdapter.VencimientoViewHolder>() {

    class VencimientoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInicial: TextView = view.findViewById(R.id.tvInicial)
        val tvNombreCompleto: TextView = view.findViewById(R.id.tvNombreCompleto)
        val tvDni: TextView = view.findViewById(R.id.tvDni)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VencimientoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_vencimiento, parent, false)
        return VencimientoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VencimientoViewHolder, position: Int) {
        val socio = socios[position]
        holder.tvNombreCompleto.text = "${socio.nombre} ${socio.apellido}"
        holder.tvDni.text = socio.dni
        // Usamos la primera letra del nombre para la inicial
        holder.tvInicial.text = socio.nombre.firstOrNull()?.toString() ?: ""
    }

    override fun getItemCount() = socios.size
}

