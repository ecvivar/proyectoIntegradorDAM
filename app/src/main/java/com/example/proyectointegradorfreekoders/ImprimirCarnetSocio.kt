package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.proyectointegradorfreekoders.database.DBHelper
import com.example.proyectointegradorfreekoders.database.Socio
import java.io.File
import java.io.FileOutputStream

class ImprimirCarnetSocio : AppCompatActivity() {

    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imprimir_carnet_socio)

        db = DBHelper(this)

        // 1. Obtenemos el ID enviado desde la lista
        val idSocio = intent.getIntExtra("idSocio", -1)

        if (idSocio == -1) {
            Toast.makeText(this, "Error al cargar los datos del socio", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 2. Obtener socio desde SQLite
        val socio = db.obtenerSocioPorId(idSocio)

        if (socio == null) {
            Toast.makeText(this, "Socio no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 3. Cargar datos en el layout
        cargarDatosEnPantalla(socio)

        // 4. Botón imprimir
        val btnImprimir = findViewById<MaterialButton>(R.id.btnImprimir)
        btnImprimir.setOnClickListener {
            // Consultar el socio desde BD por id enviado
            val socio = db.obtenerSocioPorId(idSocio)
                if (socio != null) {
                    generarPdfCarnet(socio)
                } else {
                    Toast.makeText(this, "Error: socio no encontrado", Toast.LENGTH_LONG).show()
                }
        }
    }

    // Función para cargar datos en el layout
    private fun cargarDatosEnPantalla(socio: Socio) {
        val tvNombre = findViewById<TextView>(R.id.tvNombre)
        val tvDni = findViewById<TextView>(R.id.tvDni)
        val tvSocio = findViewById<TextView>(R.id.tvSocio)

        tvNombre.text = "${socio.nombre} ${socio.apellido}"
        tvDni.text = socio.dni
        tvSocio.text = socio.id.toString()

        // Foto (si luego la guardamos en DB)
        val image = findViewById<ImageView>(R.id.imgSocio)
        image.setImageResource(R.drawable.foto_socio)
    }

    // Función para generar PDF
    private fun generarPdfCarnet(socio: Socio) {
        val pdfDocument = PdfDocument()

        // Tamaño de página A6 (ideal carnet)
        val pageInfo = PdfDocument.PageInfo.Builder(350, 550, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        val paint = Paint()

        // Fondo blanco
        paint.color = Color.WHITE
        canvas.drawRect(0f, 0f, pageInfo.pageWidth.toFloat(), pageInfo.pageHeight.toFloat(), paint)

        // Título
        paint.color = Color.BLACK
        paint.textSize = 22f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("CARNET DE SOCIO", 60f, 50f, paint)

        // Datos del socio
        paint.textSize = 16f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        canvas.drawText("Nombre: ${socio.nombre} ${socio.apellido}", 40f, 120f, paint)
        canvas.drawText("DNI: ${socio.dni}", 40f, 160f, paint)
        canvas.drawText("N° Socio: ${socio.id}", 40f, 200f, paint)
        canvas.drawText("Plan: ${socio.tipoPlan}", 40f, 240f, paint)

        pdfDocument.finishPage(page)

        // Ruta donde guardar el archivo
        val nombreArchivo = "carnet_${socio.id}.pdf"
        val file = File(getExternalFilesDir(null), nombreArchivo)

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(this, "PDF generado correctamente", Toast.LENGTH_SHORT).show()
            compartirPdf(file) // ← Abre diálogo para compartir

        } catch (e: Exception) {
            Toast.makeText(this, "Error generando PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
        pdfDocument.close()
    }

    // Función para compartir PDF
    private fun compartirPdf(file: File) {
        val uri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        startActivity(Intent.createChooser(intent, "Compartir carnet PDF"))
    }
}