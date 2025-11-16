package com.example.proyectointegradorfreekoders.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


// Data clases

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
    val foto: ByteArray?,
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
    val fechaVencimiento: String?,
    val medioPago: String
)

data class Vencimiento(
    val id: Int,
    val idSocio: Int,
    val fecha: String,
    val estado: String
)


// DBHelper
class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "club_freekoders.db"
        private const val DATABASE_VERSION = 3
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Tabla usuarios
        db.execSQL(
            """
            CREATE TABLE usuarios (
                id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre_usuario TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                rol TEXT
            )
        """
        )

        // Tabla socios
        db.execSQL(
            """
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
                foto BLOB,
                fecha_alta TEXT
            )
        """
        )

        // Tabla no socios
        db.execSQL(
            """
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
        """
        )

        // Tabla pagos
        db.execSQL(
            """
            CREATE TABLE pagos (
                id_pago INTEGER PRIMARY KEY AUTOINCREMENT,
                tipo_persona TEXT,
                id_referencia INTEGER,
                concepto TEXT,
                monto REAL,
                fecha_pago TEXT,
                fecha_vencimiento TEXT,
                medio_pago TEXT
            )
        """
        )

        // Tabla actividades
        db.execSQL(
            """
            CREATE TABLE actividades (
                id_actividad INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre_actividad TEXT,
                descripcion TEXT,
                precio REAL
            )
        """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS socios")
        db.execSQL("DROP TABLE IF EXISTS no_socios")
        db.execSQL("DROP TABLE IF EXISTS pagos")
        db.execSQL("DROP TABLE IF EXISTS actividades")
        onCreate(db)
    }


    // Metodos DAO

    // Metodos de usuario
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

    // Metodos de socio
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

    fun obtenerSocioPorId(id: Int): Socio? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM socios WHERE id_socio = ?", arrayOf(id.toString()))

        var socio: Socio? = null
        cursor.use {
            if (it.moveToFirst()) {
                val fotoBlob = if (it.isNull(it.getColumnIndexOrThrow("foto"))) {
                    null
                } else {
                    it.getBlob(it.getColumnIndexOrThrow("foto"))
                }

                socio = Socio(
                    id = it.getInt(it.getColumnIndexOrThrow("id_socio")),
                    dni = it.getString(it.getColumnIndexOrThrow("dni")),
                    nombre = it.getString(it.getColumnIndexOrThrow("nombre")),
                    apellido = it.getString(it.getColumnIndexOrThrow("apellido")),
                    telefono = it.getString(it.getColumnIndexOrThrow("telefono")),
                    direccion = it.getString(it.getColumnIndexOrThrow("direccion")),
                    email = it.getString(it.getColumnIndexOrThrow("email")),
                    tipoPlan = it.getString(it.getColumnIndexOrThrow("tipo_plan")),
                    aptoFisico = it.getInt(it.getColumnIndexOrThrow("apto_fisico")) == 1,
                    foto = fotoBlob,
                    fechaAlta = it.getString(it.getColumnIndexOrThrow("fecha_alta"))
                )
            }
        }
        return socio
    }

    fun obtenerTodosLosSocios(): List<Socio> {
        val lista = mutableListOf<Socio>()
        val db = readableDatabase

        val query = "SELECT * FROM socios ORDER BY apellido ASC"
        val cursor = db.rawQuery(query, null)

        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val fotoBlob: ByteArray? =
                        if (cursor.isNull(cursor.getColumnIndexOrThrow("foto"))) null
                        else cursor.getBlob(cursor.getColumnIndexOrThrow("foto"))

                    lista.add(Socio(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id_socio")),
                        dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                        nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                        apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                        telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                        direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion")),
                        email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        tipoPlan = cursor.getString(cursor.getColumnIndexOrThrow("tipo_plan")),
                        aptoFisico = cursor.getInt(cursor.getColumnIndexOrThrow("apto_fisico")) == 1,
                        foto = fotoBlob,
                        fechaAlta = cursor.getString(cursor.getColumnIndexOrThrow("fecha_alta"))
                    ))
                } while (cursor.moveToNext())
            }
        }
        return lista
    }

    fun buscarSocioPorDni(parcial: String): List<Socio> {
        val lista = mutableListOf<Socio>()
        val db = readableDatabase

        val query = "SELECT * FROM socios WHERE dni LIKE ? ORDER BY apellido ASC"
        val cursor = db.rawQuery(query, arrayOf("%$parcial%"))

        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val fotoBlob: ByteArray? =
                        if (cursor.isNull(cursor.getColumnIndexOrThrow("foto"))) null
                        else cursor.getBlob(cursor.getColumnIndexOrThrow("foto"))

                    lista.add(Socio(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id_socio")),
                        dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                        nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                        apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                        telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                        direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion")),
                        email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        tipoPlan = cursor.getString(cursor.getColumnIndexOrThrow("tipo_plan")),
                        aptoFisico = cursor.getInt(cursor.getColumnIndexOrThrow("apto_fisico")) == 1,
                        foto = fotoBlob,
                        fechaAlta = cursor.getString(cursor.getColumnIndexOrThrow("fecha_alta"))
                    ))
                } while (cursor.moveToNext())
            }
        }
        return lista
    }


    // Metodos de no socio
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


    fun obtenerNoSocioPorId(id: Int): NoSocio? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM no_socios WHERE id_no_socio = ?", arrayOf(id.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                val noSocio = NoSocio(
                    id = it.getInt(it.getColumnIndexOrThrow("id_no_socio")),
                    dni = it.getString(it.getColumnIndexOrThrow("dni")),
                    nombre = it.getString(it.getColumnIndexOrThrow("nombre")),
                    apellido = it.getString(it.getColumnIndexOrThrow("apellido")),
                    telefono = it.getString(it.getColumnIndexOrThrow("telefono")),
                    direccion = it.getString(it.getColumnIndexOrThrow("direccion")),
                    email = it.getString(it.getColumnIndexOrThrow("email")),
                    aptoFisico = it.getInt(it.getColumnIndexOrThrow("apto_fisico")) == 1,
                    fechaAlta = it.getString(it.getColumnIndexOrThrow("fecha_alta"))
                )
                return noSocio
            }
        }
        return null
    }

    fun obtenerTodosLosNoSocios(): List<NoSocio> {
        val lista = mutableListOf<NoSocio>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM no_socios ORDER BY apellido, nombre", null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    lista.add(
                        NoSocio(
                            id = it.getInt(it.getColumnIndexOrThrow("id_no_socio")),
                            dni = it.getString(it.getColumnIndexOrThrow("dni")),
                            nombre = it.getString(it.getColumnIndexOrThrow("nombre")),
                            apellido = it.getString(it.getColumnIndexOrThrow("apellido")),
                            telefono = it.getString(it.getColumnIndexOrThrow("telefono")),
                            direccion = it.getString(it.getColumnIndexOrThrow("direccion")),
                            email = it.getString(it.getColumnIndexOrThrow("email")),
                            aptoFisico = it.getInt(it.getColumnIndexOrThrow("apto_fisico")) == 1,
                            fechaAlta = it.getString(it.getColumnIndexOrThrow("fecha_alta"))
                        )
                    )
                } while (it.moveToNext())
            }
        }
        return lista
    }

    fun buscarNoSocioPorDni(parcial: String): List<NoSocio> {
        if(parcial.isEmpty()) return obtenerTodosLosNoSocios()

        val lista = mutableListOf<NoSocio>()
        val db = readableDatabase

        val query = "SELECT * FROM no_socios WHERE dni LIKE ? ORDER BY apellido ASC"
        val cursor = db.rawQuery(query, arrayOf("$parcial%"))

        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    lista.add(
                        NoSocio(
                            id = it.getInt(it.getColumnIndexOrThrow("id_no_socio")),
                            dni = it.getString(it.getColumnIndexOrThrow("dni")),
                            nombre = it.getString(it.getColumnIndexOrThrow("nombre")),
                            apellido = it.getString(it.getColumnIndexOrThrow("apellido")),
                            telefono = "", direccion = "", email = "",
                            aptoFisico = false, fechaAlta = ""
                        )
                    )
                } while (cursor.moveToNext())
            }
        }
        return lista
    }

    // Metodos para pagos

    fun insertarPago(pago: Pago): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("tipo_persona", pago.tipoPersona)
            put("id_referencia", pago.idReferencia)
            put("concepto", pago.concepto)
            put("monto", pago.monto)
            put("fecha_pago", pago.fechaPago)
            put("fecha_vencimiento", pago.fechaVencimiento)
            put("medio_pago", pago.medioPago)
        }
        return db.insert("pagos", null, values)
    }

    // Metodos para vencimientos

    fun getVencimientosDelDia(fechaHoy: String): Cursor {
        val db = readableDatabase
        val query = """
            SELECT s.nombre, s.apellido, s.dni, s.tipo_plan, s.id_socio FROM pagos p
            JOIN socios s ON p.id_referencia = s.id_socio AND p.tipo_persona = 'socio'
            WHERE p.fecha_vencimiento = ?
            ORDER BY s.apellido, s.nombre
        """
        return db.rawQuery(query, arrayOf(fechaHoy))
    }


    fun getPagosSocioPorDNI(dni: String): Cursor {
        val db = readableDatabase
        val query = """
            SELECT p.concepto, p.monto, p.fecha_pago 
            FROM pagos p
            JOIN socios s ON p.id_referencia = s.id_socio AND p.tipo_persona = 'socio'
            WHERE s.dni = ?
            ORDER BY p.fecha_pago DESC
        """
        return db.rawQuery(query, arrayOf(dni))
    }

    fun getPagosNoSocioPorDNI(dni: String): Cursor {
        val db = readableDatabase
        val query = """
            SELECT p.concepto, p.monto, p.fecha_pago 
            FROM pagos p
            JOIN no_socios ns ON p.id_referencia = ns.id_no_socio AND p.tipo_persona = 'no_socio'
            WHERE ns.dni = ?
            ORDER BY p.fecha_pago DESC
        """
        return db.rawQuery(query, arrayOf(dni))
    }

}