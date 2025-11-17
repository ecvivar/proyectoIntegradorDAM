package com.example.proyectointegradorfreekoders.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectointegradorfreekoders.database.ItemClientePago // Asegúrate de que el import sea correcto
import com.example.proyectointegradorfreekoders.R

class ListaPagoAdapter(
    private var items: MutableList<ItemClientePago>,
    private val onClick: (ItemClientePago) -> Unit
) : RecyclerView.Adapter<ListaPagoAdapter.PagoViewHolder>() {

    // Describe la vista de un ítem y sus metadatos dentro del RecyclerView.
    class PagoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // IDs de los componentes del layout de la fila
        private val nombreTextView: TextView = view.findViewById(R.id.tvNombreCliente)
        private val dniTextView: TextView = view.findViewById(R.id.tvDniCliente)
        private val tipoTextView: TextView = view.findViewById(R.id.tvTipoCliente)

        fun bind(item: ItemClientePago, onClick: (ItemClientePago) -> Unit) {
            nombreTextView.text = item.nombreCompleto
            dniTextView.text = "DNI: ${item.dni}"
            // Reemplaza el guion bajo para que se vea más limpio en la UI
            tipoTextView.text = item.tipoCliente.replace("_", " ")

            // Asigna el evento de clic a toda la fila
            itemView.setOnClickListener { onClick(item) }
        }
    }

    // Se llama cuando el RecyclerView necesita un nuevo ViewHolder para representar un ítem.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagoViewHolder {
        // "Infla" (crea) la vista de la fila desde el archivo XML
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente_pago, parent, false)
        return PagoViewHolder(view)
    }

    // Se llama para mostrar los datos en una posición específica.
    override fun onBindViewHolder(holder: PagoViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onClick)
    }

    // Devuelve el número total de ítems en la lista.
    override fun getItemCount(): Int = items.size

    // Función para actualizar la lista de ítems en el adaptador.
    fun updateList(newList: List<ItemClientePago>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }
}
