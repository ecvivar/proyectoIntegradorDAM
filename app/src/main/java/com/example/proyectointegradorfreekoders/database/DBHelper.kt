package com.example.proyectointegradorfreekoders.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// ---------------------
// 1. Data Classes
// ---------------------
data class Usuario(
    val id: Int,
    val nombre_usuario: String,
    val password: String,
    val rol: String?
)

data class Socio(
    val id: Int,
    val dni: String,
    val nombre: String,
    val apellido: String,
    val telefono: String?,
    val direccion: String?,
    val email: String?,
    val tipoPlan: String,
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
    val direccion: String?,
    val email: String?,
    val aptoFisico: Boolean,
    val fechaAlta: String
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
        private const val DATABASE_VERSION = 2 // incrementado
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Tabla usuarios
        db.execSQL("""
            CREATE TABLE usuarios (
                id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre_usuario TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                rol TEXT
            )
        """)

        // Tabla socios (ahora con email y tipo_plan)
        db.execSQL("""
            CREATE TABLE socios (
                id_socio INTEGER PRIMARY KEY AUTOINCREMENT,
                dni TEXT UNIQUE,
                nombre TEXT,
                apellido TEXT,
                telefono TEXT,
                direccion TEXT,
                email TEXT,
                tipo_plan TEXT,
                apto_fisico INTEGER,
                foto TEXT,
                fecha_alta TEXT
            )
        """)

        // Tabla no socios (con email)
        db.execSQL("""
            CREATE TABLE no_socios (
                id_no_socio INTEGER PRIMARY KEY AUTOINCREMENT,
                dni TEXT UNIQUE,
                nombre TEXT,
                apellido TEXT,
                telefono TEXT,
                direccion TEXT,
                email TEXT,
                apto_fisico INTEGER,
                fecha_alta TEXT
            )
        """)

        // Tabla pagos
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

        // Tabla actividades
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
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS socios")
        db.execSQL("DROP TABLE IF EXISTS no_socios")
        db.execSQL("DROP TABLE IF EXISTS pagos")
        db.execSQL("DROP TABLE IF EXISTS actividades")
        onCreate(db)
    }

    // ---------------------
    // 3. Métodos DAO
    // ---------------------

    // USUARIOS
    fun insertarUsuario(usuario: Usuario): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre_usuario", usuario.nombre_usuario)
            put("password", usuario.password)
            put("rol", usuario.rol)
        }
        return db.insert("usuarios", null, values)
    }

    fun validarUsuario(nombre_usuario: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE nombre_usuario = ? AND password = ?",
            arrayOf(nombre_usuario, password)
        )
        val existe = cursor.moveToFirst()
        cursor.close()
        return existe
    }

    // SOCIOS
    fun insertarSocio(socio: Socio): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("dni", socio.dni)
            put("nombre", socio.nombre)
            put("apellido", socio.apellido)
            put("telefono", socio.telefono)
            put("direccion", socio.direccion)
            put("email", socio.email)
            put("tipo_plan", socio.tipoPlan)
            put("apto_fisico", if (socio.aptoFisico) 1 else 0)
            put("foto", socio.foto)
            put("fecha_alta", socio.fechaAlta)
        }
        return db.insert("socios", null, values)
    }

    // NO SOCIOS
    fun insertarNoSocio(noSocio: NoSocio): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("dni", noSocio.dni)
            put("nombre", noSocio.nombre)
            put("apellido", noSocio.apellido)
            put("telefono", noSocio.telefono)
            put("direccion", noSocio.direccion)
            put("email", noSocio.email)
            put("apto_fisico", if (noSocio.aptoFisico) 1 else 0)
            put("fecha_alta", noSocio.fechaAlta)
        }
        return db.insert("no_socios", null, values)
    }

    // Obtener Socios
    fun obtenerSocioPorId(id: Int): Socio? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM socios WHERE id_socio = ?", arrayOf(id.toString()))

        return if (cursor.moveToFirst()) {
            Socio(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id_socio")),
                dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                tipoPlan = cursor.getString(cursor.getColumnIndexOrThrow("tipo_plan")),
                aptoFisico = cursor.getInt(cursor.getColumnIndexOrThrow("apto_fisico")) == 1,
                foto = cursor.getString(cursor.getColumnIndexOrThrow("foto")),
                fechaAlta = cursor.getString(cursor.getColumnIndexOrThrow("fecha_alta")),
            )
        } else null
    }
    // Obtener todos los socios
    fun obtenerTodosLosSocios(): List<Socio> {
        val lista = mutableListOf<Socio>()
        val db = readableDatabase

        val cursor = db.rawQuery("SELECT * FROM socios ORDER BY apellido, nombre", null)

        if (cursor.moveToFirst()) {
            do {
                val socio = Socio(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id_socio")),
                    dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                    telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                    direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion")),
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    tipoPlan = cursor.getString(cursor.getColumnIndexOrThrow("tipo_plan")),
                    aptoFisico = cursor.getInt(cursor.getColumnIndexOrThrow("apto_fisico")) == 1,
                    foto = cursor.getString(cursor.getColumnIndexOrThrow("foto")),
                    fechaAlta = cursor.getString(cursor.getColumnIndexOrThrow("fecha_alta"))
                )
                lista.add(socio)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return lista
    }
}