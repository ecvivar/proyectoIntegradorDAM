package com.example.proyectointegradorfreekoders

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.widget.Toast
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import android.content.Intent

class ImprimirCarnetSocio : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imprimir_carnet_socio)

        val contenedorDatos = findViewById<View>(R.id.contenedorDatos)
        val btnImprimir = findViewById<View>(R.id.btnImprimir)

        btnImprimir.setOnClickListener {
            generarPDFDesdeVista(contenedorDatos, this)
        }
    }

    private fun generarPDFDesdeVista(view: View, context: Context) {
        // Crear un documento PDF
        val document = PdfDocument()

        // Medir el tamaño de la vista
        val pageInfo = PdfDocument.PageInfo.Builder(
            view.width, view.height, 1
        ).create()

        val page = document.startPage(pageInfo)

        // Dibujar el contenido del layout sobre el PDF
        view.draw(page.canvas)
        document.finishPage(page)

        // Crear un nombre de archivo con fecha
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "CarnetSocio_$timeStamp.pdf"

        // Guardar en la carpeta Descargas
        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)

        try {
            document.writeTo(FileOutputStream(file))
            //mensaje
            Toast.makeText(context, "PDF guardado en: ${file.absolutePath}", Toast.LENGTH_LONG).show()
            //volver al inicio
            val intent = Intent(this, MenuPrincipal::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error al generar el PDF: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            document.close()
        }
    }
}