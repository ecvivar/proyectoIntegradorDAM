package com.example.proyectointegradorfreekoders

import android.content.Context
import android.database.Cursor
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectointegradorfreekoders.adapters.VencimientoAdapter
import com.example.proyectointegradorfreekoders.database.DBHelper
import com.example.proyectointegradorfreekoders.database.SocioConDeuda
import com.google.android.material.button.MaterialButton
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar

class ListarVencimientosActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var rvVencimientos: RecyclerView
    private lateinit var layoutParaImprimir: View // Variable para guardar la vista a imprimir

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listarvencimientos)

        // Inicialización de componentes
        db = DBHelper(this)
        rvVencimientos = findViewById(R.id.rvVencimientos)
        layoutParaImprimir = findViewById(R.id.layout_para_imprimir) // Asignamos la vista
        rvVencimientos.layoutManager = LinearLayoutManager(this)

        // Configurar botones
        findViewById<MaterialButton>(R.id.btnVolver).setOnClickListener { finish() }
        findViewById<Button>(R.id.btnImprimir).setOnClickListener {
            // Pasamos el contexto y la vista que queremos imprimir
            imprimirLista(this, layoutParaImprimir)
        }

        // Cargar los datos en la lista
        cargarVencimientos()
    }

    private fun cargarVencimientos() {val calendario = Calendar.getInstance()
        val anioActual = calendario.get(Calendar.YEAR)
        val mesActual = calendario.get(Calendar.MONTH) + 1

        // 1. Obtenemos el cursor desde la base de datos.
        val cursor: Cursor = db.getSociosConCuotasVencidas(anioActual, mesActual)

        // 2. Creamos la lista que vamos a rellenar.
        val sociosConDeuda = mutableListOf<SocioConDeuda>()

        // 3. Verificamos que el cursor no sea nulo y que tenga datos.
        if (cursor.moveToFirst()) {
            try {
                // 4. Obtenemos los índices de las columnas UNA SOLA VEZ antes del bucle.
                val nombreIndex = cursor.getColumnIndexOrThrow("nombre")
                val apellidoIndex = cursor.getColumnIndexOrThrow("apellido")
                val dniIndex = cursor.getColumnIndexOrThrow("dni")

                // 5. Recorremos el cursor con un bucle do-while.
                do {
                    val socio = SocioConDeuda(
                        nombre = cursor.getString(nombreIndex),
                        apellido = cursor.getString(apellidoIndex),
                        dni = cursor.getString(dniIndex)
                    )
                    sociosConDeuda.add(socio)
                } while (cursor.moveToNext())

            } finally {
                // 6. ¡MUY IMPORTANTE! Cerramos el cursor para liberar recursos.
                cursor.close()
            }
        }

        // 7. Creamos y asignamos el adaptador al RecyclerView.
        val adapter = VencimientoAdapter(sociosConDeuda)
        rvVencimientos.adapter = adapter
    }


    private fun imprimirLista(context: Context, view: View) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val jobName = "${context.getString(R.string.app_name)} - Reporte de Vencimientos"

        // Creamos un adaptador de impresión personalizado
        val printAdapter = object : PrintDocumentAdapter() {

            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes,
                cancellationSignal: CancellationSignal?,
                callback: LayoutResultCallback,
                extras: Bundle?
            ) {
                if (cancellationSignal?.isCanceled == true) {
                    callback.onLayoutCancelled()
                    return
                }
                // Definimos la información del documento (1 página en este caso)
                val info = PrintDocumentInfo.Builder(jobName)
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(1)
                    .build()

                callback.onLayoutFinished(info, true)
            }

            override fun onWrite(
                pages: Array<out PageRange>?,
                destination: ParcelFileDescriptor,
                cancellationSignal: CancellationSignal?,
                callback: WriteResultCallback
            ) {
                if (cancellationSignal?.isCanceled == true) {
                    callback.onWriteCancelled()
                    return
                }

                val document = PdfDocument()
                val pageInfo = PdfDocument.PageInfo.Builder(view.width, view.height, 1).create()
                val page = document.startPage(pageInfo)

                // Dibujamos el contenido de la vista en el lienzo (Canvas) de la página PDF
                view.draw(page.canvas)
                document.finishPage(page)

                try {
                    FileOutputStream(destination.fileDescriptor).use {
                        document.writeTo(it)
                    }
                } catch (e: IOException) {
                    callback.onWriteFailed(e.toString())
                    return
                } finally {
                    document.close()
                }

                callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
            }
        }

        printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
        Toast.makeText(context, "Preparando para imprimir...", Toast.LENGTH_SHORT).show()
    }
}
