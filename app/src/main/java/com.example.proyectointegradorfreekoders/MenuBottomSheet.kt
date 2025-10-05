package com.example.proyectointegradorfreekoders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MenuBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_menu, container, false)

        // Ejemplo de acciones
        view.findViewById<Button>(R.id.btnListaPagos).setOnClickListener {
            Toast.makeText(requireContext(), "Abrir lista de pagos", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        view.findViewById<Button>(R.id.btnVencimientosDia).setOnClickListener {
            Toast.makeText(requireContext(), "Listar vencimientos del día", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        view.findViewById<Button>(R.id.btnImprimirCarnet).setOnClickListener {
            Toast.makeText(requireContext(), "Imprimir carnet de socio", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        view.findViewById<Button>(R.id.btnAsignarActividad).setOnClickListener {
            Toast.makeText(requireContext(), "Asignar actividad (No Socio)", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        return view
    }
}
