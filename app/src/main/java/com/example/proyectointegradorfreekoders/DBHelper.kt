package com.example.proyectointegradorfreekoders

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// ---------------------
// 1. Data Classes
// ---------------------
data class Usuario(
    val id: Int,
    val nombreUsuario: String,
    val password: String,
    val rol: String?
)

data class Socio(
    val id: Int,
    val dni: String,
    val nombre: String,
    val apellido: String,
    val telefono: String?,
    val email: String?,
    val direccion: String?,
    val aptoFisico: Boolean,
    val foto: String?,
    val fechaAlta: String
)

data class NoSocio(
    val id: Int,
    val dni: String,
    val nombre: String,
    val apellido: String,
    val telefono: String?,
    val email: String?
)

data class Actividad(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val precio: Double
)

data class Pago(
    val id: Int,
    val tipoPersona: String,
    val idReferencia: Int,
    val concepto: String,
    val monto: Double,
    val fechaPago: String,
    val medioPago: String
)

data class Vencimiento(
    val id: Int,
    val idSocio: Int,
    val fecha: String,
    val estado: String
)

// ---------------------
// 2. DBHelper
// ---------------------
class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "club_freekoders.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
    CREATE TABLE usuarios (
        id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
        nombre_usuario TEXT UNIQUE NOT NULL,
        password TEXT NOT NULL,
        rol TEXT
    )
""")
        db.execSQL("""
            CREATE TABLE socios (
                id_socio INTEGER PRIMARY KEY AUTOINCREMENT,
                dni TEXT UNIQUE,
                nombre TEXT,
                apellido TEXT,
                telefono TEXT,
                email TEXT,
                direccion TEXT,
                apto_fisico INTEGER,
                foto TEXT,
                fecha_alta TEXT
            )
        """)
        db.execSQL("""
            CREATE TABLE pagos (
                id_pago INTEGER PRIMARY KEY AUTOINCREMENT,
                tipo_persona TEXT,
                id_referencia INTEGER,
                concepto TEXT,
                monto REAL,
                fecha_pago TEXT,
                medio_pago TEXT
            )
        """)
        db.execSQL("""
            CREATE TABLE actividades (
                id_actividad INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre_actividad TEXT,
                descripcion TEXT,
                precio REAL
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS socios")
        db.execSQL("DROP TABLE IF EXISTS pagos")
        db.execSQL("DROP TABLE IF EXISTS actividades")
        onCreate(db)
    }

    // ---------------------
    // Métodos DAO
    // ---------------------

    // =============================
// USUARIOS
// =============================

    fun insertarUsuario(usuario: Usuario): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre_usuario", usuario.nombreUsuario)
            put("password", usuario.password)
            put("rol", usuario.rol)
        }
        return db.insert("usuarios", null, values)
    }

    fun validarUsuario(nombreUsuario: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE nombre_usuario = ? AND password = ?",
            arrayOf(nombreUsuario, password)
        )
        val existe = cursor.moveToFirst()
        cursor.close()
        return existe
    }

    fun obtenerUsuarios(): List<Usuario> {
        val db = readableDatabase
        val lista = mutableListOf<Usuario>()
        val cursor = db.rawQuery("SELECT * FROM usuarios", null)
        cursor.use {
            while (it.moveToNext()) {
                lista.add(
                    Usuario(
                        id = it.getInt(it.getColumnIndexOrThrow("id_usuario")),
                        nombreUsuario = it.getString(it.getColumnIndexOrThrow("nombre_usuario")),
                        password = it.getString(it.getColumnIndexOrThrow("password")),
                        rol = it.getString(it.getColumnIndexOrThrow("rol"))
                    )
                )
            }
        }
        return lista
    }

    fun insertarSocio(socio: Socio): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("dni", socio.dni)
            put("nombre", socio.nombre)
            put("apellido", socio.apellido)
            put("telefono", socio.telefono)
            put("email", socio.email)
            put("direccion", socio.direccion)
            put("apto_fisico", if (socio.aptoFisico) 1 else 0)
            put("foto", socio.foto)
            put("fecha_alta", socio.fechaAlta)
        }
        return db.insert("socios", null, values)
    }

    fun obtenerSocios(): List<Socio> {
        val db = readableDatabase
        val lista = mutableListOf<Socio>()
        val cursor = db.rawQuery("SELECT * FROM socios", null)
        cursor.use {
            while (it.moveToNext()) {
                lista.add(
                    Socio(
                        id = it.getInt(it.getColumnIndexOrThrow("id_socio")),
                        dni = it.getString(it.getColumnIndexOrThrow("dni")),
                        nombre = it.getString(it.getColumnIndexOrThrow("nombre")),
                        apellido = it.getString(it.getColumnIndexOrThrow("apellido")),
                        telefono = it.getString(it.getColumnIndexOrThrow("telefono")),
                        email = it.getString(it.getColumnIndexOrThrow("email")),
                        direccion = it.getString(it.getColumnIndexOrThrow("direccion")),
                        aptoFisico = it.getInt(it.getColumnIndexOrThrow("apto_fisico")) == 1,
                        foto = it.getString(it.getColumnIndexOrThrow("foto")),
                        fechaAlta = it.getString(it.getColumnIndexOrThrow("fecha_alta"))
                    )
                )
            }
        }
        return lista
    }

    fun insertarPago(pago: Pago): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("tipo_persona", pago.tipoPersona)
            put("id_referencia", pago.idReferencia)
            put("concepto", pago.concepto)
            put("monto", pago.monto)
            put("fecha_pago", pago.fechaPago)
            put("medio_pago", pago.medioPago)
        }
        return db.insert("pagos", null, values)
    }

    fun obtenerPagosPorSocio(idSocio: Int): List<Pago> {
        val db = readableDatabase
        val lista = mutableListOf<Pago>()
        val cursor = db.rawQuery(
            "SELECT * FROM pagos WHERE tipo_persona = 'socio' AND id_referencia = ?",
            arrayOf(idSocio.toString())
        )
        cursor.use {
            while (it.moveToNext()) {
                lista.add(
                    Pago(
                        id = it.getInt(it.getColumnIndexOrThrow("id_pago")),
                        tipoPersona = it.getString(it.getColumnIndexOrThrow("tipo_persona")),
                        idReferencia = it.getInt(it.getColumnIndexOrThrow("id_referencia")),
                        concepto = it.getString(it.getColumnIndexOrThrow("concepto")),
                        monto = it.getDouble(it.getColumnIndexOrThrow("monto")),
                        fechaPago = it.getString(it.getColumnIndexOrThrow("fecha_pago")),
                        medioPago = it.getString(it.getColumnIndexOrThrow("medio_pago"))
                    )
                )
            }
        }
        return lista
    }
}
