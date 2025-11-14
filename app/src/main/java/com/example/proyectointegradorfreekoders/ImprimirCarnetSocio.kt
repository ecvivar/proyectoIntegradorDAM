package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.proyectointegradorfreekoders.database.DBHelper
import com.example.proyectointegradorfreekoders.database.Socio
import com.google.android.material.button.MaterialButton
import java.io.File
import java.io.FileOutputStream

class ImprimirCarnetSocio : AppCompatActivity() {

    private lateinit var db: DBHelper
    private val REQUEST_SHARE_PDF = 2001  // Código para el resultado del share

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imprimir_carnet_socio)

        db = DBHelper(this)

        // Obtiene el ID del socio enviado desde la lista
        val idSocio = intent.getIntExtra("idSocio", -1)

        if (idSocio == -1) {
            Toast.makeText(this, "Error al cargar los datos del socio", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Obtener socio desde SQLite
        val socio = db.obtenerSocioPorId(idSocio)

        if (socio == null) {
            Toast.makeText(this, "Socio no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        cargarDatosEnPantalla(socio)

        // Botón imprimir
        val btnImprimir = findViewById<MaterialButton>(R.id.btnImprimir)
        btnImprimir.setOnClickListener {
            generarPdfDesdeLayout()
        }
    }

    private fun cargarDatosEnPantalla(socio: Socio) {
        findViewById<TextView>(R.id.tvNombre).text = "${socio.nombre} ${socio.apellido}"
        findViewById<TextView>(R.id.tvDni).text = socio.dni
        findViewById<TextView>(R.id.tvSocio).text = socio.id.toString()

        val image = findViewById<ImageView>(R.id.imgSocio)
        image.setImageResource(R.drawable.foto_socio)
    }

    // Convertir layout a bitmap
    private fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    // Generar PDF desde layout
    private fun generarPdfDesdeLayout() {

        val contenedor = findViewById<View>(R.id.contenedorDatos)

        contenedor.post {
            val bitmap = viewToBitmap(contenedor)

            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page = document.startPage(pageInfo)

            val canvas = page.canvas
            canvas.drawBitmap(bitmap, 0f, 0f, null)

            document.finishPage(page)

            val file = File(getExternalFilesDir(null), "carnet_socio.pdf")
            document.writeTo(FileOutputStream(file))
            document.close()

            compartirPdf(file) // Ahora el share maneja el retorno
        }
    }

    // Compartir PDF con callback
    private fun compartirPdf(file: File) {

        val uri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(intent, "Compartir carnet")

        startActivityForResult(chooser, REQUEST_SHARE_PDF)
    }

    // Volver al menú después de compartir
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SHARE_PDF) {
            mostrarMensajeYVolver()
        }
    }

    private fun mostrarMensajeYVolver() {
        Toast.makeText(this, "Carnet generado con éxito", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MenuPrincipal::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }, 1500)
    }
}