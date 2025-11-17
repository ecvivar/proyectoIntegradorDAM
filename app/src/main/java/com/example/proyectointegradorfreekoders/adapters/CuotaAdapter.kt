package com.example.proyectointegradorfreekoders.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectointegradorfreekoders.R
import com.example.proyectointegradorfreekoders.database.Cuotas
import com.example.proyectointegradorfreekoders.database.EstadoCuotas

class CuotaAdapter(
    private var items: MutableList<Cuotas>
) : RecyclerView.Adapter<CuotaAdapter.CuotaViewHolder>() {

    // Guarda las cuotas que el usuario va seleccionando con el checkbox
    private val cuotasSeleccionadas = mutableSetOf<Cuotas>()

    /**
     * ViewHolder: Representa una única fila (un ítem) en la lista.
     */
    class CuotaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMesAnio: TextView = view.findViewById(R.id.tvMesAnio)
        val tvEstado: TextView = view.findViewById(R.id.tvEstadoCuota)
        val cbPagar: CheckBox = view.findViewById(R.id.cbPagar)

        fun bind(cuota: Cuotas) {
            tvMesAnio.text = "Cuota: ${cuota.mes}/${cuota.anio}"
            val estado = cuota.obtenerEstado()
            tvEstado.text = estado.name // Muestra "PAGADA", "VENCIDA", etc.

            // Asigna un color diferente según el estado de la cuota
            when (estado) {
                EstadoCuotas.PAGADA -> tvEstado.setTextColor(Color.parseColor("#4CAF50")) // Verde
                EstadoCuotas.VENCIDA -> tvEstado.setTextColor(Color.parseColor("#F44336")) // Rojo
                EstadoCuotas.PROXIMA -> tvEstado.setTextColor(Color.parseColor("#FF9800")) // Naranja
                EstadoCuotas.PENDIENTE -> tvEstado.setTextColor(Color.DKGRAY)
            }

            // El checkbox es invisible para las cuotas ya pagadas
            cbPagar.visibility = if (estado == EstadoCuotas.PAGADA) View.INVISIBLE else View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuotaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cuota, parent, false)
        return CuotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CuotaViewHolder, position: Int) {
        val cuota = items[position]
        holder.bind(cuota)

        // Sincroniza el estado del checkbox con nuestra lista de seleccionados
        holder.cbPagar.setOnCheckedChangeListener(null) // Evita loops al reciclar vistas
        holder.cbPagar.isChecked = cuotasSeleccionadas.contains(cuota)

        // Define la lógica cuando el usuario marca o desmarca el checkbox
        holder.cbPagar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cuotasSeleccionadas.add(cuota)
            } else {
                cuotasSeleccionadas.remove(cuota)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    /**
     * Función pública para actualizar la lista de cuotas desde la Activity.
     */
    fun updateList(newList: List<Cuotas>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    /**
     * Función pública para que la Activity pueda obtener las cuotas seleccionadas.
     */
    fun obtenerCuotasSeleccionadas(): List<Cuotas> {
        // Devuelve la lista ordenada por fecha
        return cuotasSeleccionadas.toList().sortedBy { it.anio * 100 + it.mes }
    }
}
