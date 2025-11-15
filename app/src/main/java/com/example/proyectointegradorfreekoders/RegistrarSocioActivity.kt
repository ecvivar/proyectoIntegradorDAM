package com.example.proyectointegradorfreekoders

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.example.proyectointegradorfreekoders.database.DBHelper
import com.example.proyectointegradorfreekoders.database.Socio
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegistrarSocioActivity : AppCompatActivity() {

    // Variables para la foto
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private var fotoEnBytes: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_socio)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar la base de datos
        val db = DBHelper(this)

        // Obtener fecha actual
        val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Botón volver atrás
        val botonVolver = findViewById<MaterialButton>(R.id.btnRegistrarSocio)
        botonVolver.setOnClickListener {
            finish()
        }

        // Spinner (AutoCompleteTextView) para Tipo de Plan
        val autoTipoPlan = findViewById<AutoCompleteTextView>(R.id.txtPlan)
        val planes = resources.getStringArray(R.array.tipoPlan)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, planes)
        autoTipoPlan.setAdapter(adapter)

        // Registrar launcher para cámara
        takePictureLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as? Bitmap
                if (bitmap != null) {
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                    fotoEnBytes = stream.toByteArray()

                    val img = findViewById<ImageView>(R.id.imgPreview)
                    img.setImageBitmap(bitmap)

                    Toast.makeText(this, "Foto cargada correctamente", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Botón tomar foto
        val btnFoto = findViewById<MaterialButton>(R.id.btnSubirFotoCarnet)
        btnFoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureLauncher.launch(intent)
        }

        // Botón final de registro
        val btnFinal = findViewById<MaterialButton>(R.id.btnRegistrarSocioFinal)

        btnFinal.setOnClickListener {
            try {
                // Obtener datos de formulario
                val nombre = findViewById<EditText>(R.id.txtNombre).text.toString().trim()
                val apellido = findViewById<EditText>(R.id.txtApellido).text.toString().trim()
                val telefono = findViewById<EditText>(R.id.txtTelefono).text.toString().trim()
                val direccion = findViewById<EditText>(R.id.txtDireccion).text.toString().trim()
                val email = findViewById<EditText>(R.id.txtEmail).text.toString().trim()
                val dni = findViewById<EditText>(R.id.txtDocumento).text.toString().trim()
                val tipoPlan = autoTipoPlan.text.toString().trim()
                val aptoFisico = findViewById<CheckBox>(R.id.cbAptoFisico).isChecked

                // Validaciones básicas
                if (nombre.isEmpty() || apellido.isEmpty() ||
                    telefono.isEmpty() || dni.isEmpty() || tipoPlan.isEmpty()
                ) {
                    Toast.makeText(
                        this,
                        "Complete todos los campos obligatorios",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                // Crear objeto socio con FOTO incluida
                val socio = Socio(
                    id = 0,
                    dni = dni,
                    nombre = nombre,
                    apellido = apellido,
                    telefono = telefono,
                    direccion = direccion,
                    email = email,
                    tipoPlan = tipoPlan,
                    aptoFisico = aptoFisico,
                    foto = fotoEnBytes,   // ← FOTO GUARDADA AQUÍ
                    fechaAlta = fechaActual
                )

                // Guardar en la base de datos
                val resultado = db.insertarSocio(socio)

                if (resultado != -1L) {
                    startActivity(Intent(this, RegistrarSocioCorrectoActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Error al registrar socio", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Ocurrió un error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}