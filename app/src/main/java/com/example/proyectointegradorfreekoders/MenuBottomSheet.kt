package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class MenuBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.bottom_menu, container, false)

        val botonListaDePagos = view.findViewById<MaterialButton>(R.id.btnListaPagos)
        botonListaDePagos.setOnClickListener {
            val intent = Intent(requireContext(), ListaDePagoInicioActivity::class.java)
            startActivity(intent)
            dismiss()
        }

        val botonVencimientos = view.findViewById<MaterialButton>(R.id.btnVencimientosDia)
        botonVencimientos.setOnClickListener {
            val intent = Intent(requireContext(), ListarVencimientosActivity::class.java)
            startActivity(intent)
            dismiss()
        }


        val botonAsignarActividadNoSocio = view.findViewById<MaterialButton>(R.id.btnAsignarActividad)
        botonAsignarActividadNoSocio.setOnClickListener {
            val intent = Intent(requireContext(), AsignarActividadNoSocioActivity::class.java)
                startActivity(intent)
                dismiss()
            }

        val btnImprimirCarnet = view.findViewById<MaterialButton>(R.id.btnImprimirCarnet)
        btnImprimirCarnet.setOnClickListener {
            val intent = Intent(requireContext(), BuscarSocioImprimirActivity::class.java)
                startActivity(intent)
                dismiss()
            }

        return view
    }
}