package com.example.proyectointegradorfreekoders.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDate
import java.time.temporal.ChronoUnit

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
    val medioPago: String
)

data class Cuotas(
    val id: Int,
    val socioId: Int,
    val mes: Int,
    val anio: Int,
    val pagado: Int
) {
    fun obtenerEstado(): EstadoCuotas {
        if (pagado == 1) return EstadoCuotas.PAGADA

        val hoy = LocalDate.now()
        val fechaCuota = LocalDate.of(anio, mes, 1)
        val vencimiento = fechaCuota.plusMonths(1)

        return when {
            vencimiento.isBefore(hoy) -> EstadoCuotas.VENCIDA
            ChronoUnit.DAYS.between(hoy, vencimiento) <= 30 -> EstadoCuotas.PROXIMA
            else -> EstadoCuotas.PENDIENTE
        }
    }
}

enum class EstadoCuotas {
    PAGADA, VENCIDA, PROXIMA, PENDIENTE
}

data class ItemClientePago(
    val id: Int,
    val dni: String,
    val nombreCompleto: String,
    val tipoCliente: String, // "SOCIO" o "NO_SOCIO"
)
// ---------------------
// 2. DBHelper
// ---------------------
class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "club_freekoders.db"
        private const val DATABASE_VERSION = 5 // incrementado
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

        // Tabla socios (ahora con email y tipo_plan)
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
                foto ,
                fecha_alta TEXT
            )
        """
        )

        // Tabla no socios (con email)
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
                medio_pago TEXT
            )
        """
        )

        // Tabla cuotas
        db.execSQL(
            """
            CREATE TABLE cuotas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                socio_id INTEGER,
                mes INTEGER,
                anio INTEGER,
                pagado INTEGER DEFAULT 0,
                FOREIGN KEY(socio_id) REFERENCES socios(id)
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

        // Insertar los datos de prueba después de crear las tablas.
        insertarDatosIniciales(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE socios ADD COLUMN foto BLOB")
        }
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS socios")
        db.execSQL("DROP TABLE IF EXISTS no_socios")
        db.execSQL("DROP TABLE IF EXISTS pagos")
        db.execSQL("DROP TABLE IF EXISTS actividades")
        onCreate(db)
    }

    private fun insertarDatosIniciales(db: SQLiteDatabase) {
        // Insertar socios
        val socios = listOf(
            Socio(
                1,
                "20123456",
                "Juan",
                "Pérez",
                "1122334455",
                "Calle 123",
                "juan.perez@email.com",
                "Básico",
                true,
                null,
                "2024-01-15"
            ),
            Socio(
                2,
                "21234567",
                "María",
                "Gómez",
                "1133445566",
                "Av. Libertador 456",
                "maria.gomez@email.com",
                "Premium",
                true,
                null,
                "2024-05-01"
            ),
            Socio(
                3,
                "22345678",
                "Carlos",
                "López",
                "1144556677",
                "Rivadavia 789",
                "carlos.lopez@email.com",
                "Básico",
                false,
                null,
                "2024-10-20"
            ),
            Socio(
                4,
                "23456789",
                "Ana",
                "Rodríguez",
                "1155667788",
                "Sarmiento 101",
                "ana.rodriguez@email.com",
                "Intermedio",
                true,
                null,
                "2024-07-05"
            ),
            Socio(
                5,
                "24567890",
                "Pedro",
                "Martínez",
                "1166778899",
                "Corrientes 202",
                "pedro.martinez@email.com",
                "Premium",
                true,
                null,
                "2024-11-01"
            ),
            Socio(
                6,
                "25678901",
                "Lucía",
                "Fernández",
                "1177889900",
                "Belgrano 303",
                "lucia.f@email.com",
                "Intermedio",
                true,
                null,
                "2025-01-28"
            ),
            Socio(
                7,
                "26789012",
                "Jorge",
                "Díaz",
                "1188990011",
                "San Martín 404",
                "jorge.d@email.com",
                "Básico",
                false,
                null,
                "2025-03-10"
            ),
            Socio(
                8,
                "27890123",
                "Sofía",
                "Morales",
                "1199001122",
                "Alvear 505",
                "sofia.m@email.com",
                "Premium",
                true,
                null,
                "2024-08-12"
            ),
            Socio(
                9,
                "28901234",
                "Martín",
                "Ruiz",
                "1100112233",
                "9 de Julio 606",
                "martin.r@email.com",
                "Básico",
                true,
                null,
                "2025-04-01"
            ),
            Socio(
                10,
                "29012345",
                "Elena",
                "Castro",
                "1122334400",
                "Santa Fe 707",
                "elena.c@email.com",
                "Intermedio",
                true,
                null,
                "2025-05-20"
            )
        )

        for (socio in socios) {
            val values = ContentValues().apply {
                // No se inserta el ID porque es AUTOINCREMENT
                put("dni", socio.dni)
                put("nombre", socio.nombre)
                put("apellido", socio.apellido)
                put("telefono", socio.telefono)
                put("direccion", socio.direccion)
                put("email", socio.email)
                put("tipo_plan", socio.tipoPlan)
                put("apto_fisico", if (socio.aptoFisico) 1 else 0)
                // Se usa null para la foto en datos de prueba
                put("foto", socio.foto)
                put("fecha_alta", socio.fechaAlta)
            }
            // Importante: usar db.insert en lugar del método insertarSocio() de la clase.
            val idGenerado = db.insert("socios", null, values)

            // Generar cuotas iniciales para el socio, usando el ID generado por SQLite
            generarCuotasAutomaticas(db, idGenerado.toInt(), socio.fechaAlta)
        }

        // 3. Insertar No Socios
        val noSocios = listOf(
            NoSocio(
                1,
                "30123456",
                "Gabriel",
                "Torres",
                "1112345678",
                "Lavalle 111",
                "gabriel.t@email.com",
                true,
                "2025-06-01"
            ),
            NoSocio(
                2,
                "31234567",
                "Laura",
                "Acosta",
                "1123456789",
                "Junín 222",
                "laura.a@email.com",
                true,
                "2025-06-15"
            ),
            NoSocio(
                3,
                "32345678",
                "Ricardo",
                "Herrera",
                "1134567890",
                "Córdoba 333",
                "ricardo.h@email.com",
                false,
                "2025-07-01"
            ),
            NoSocio(
                4,
                "33456789",
                "Viviana",
                "Luna",
                "1145678901",
                "Entre Ríos 444",
                "viviana.l@email.com",
                true,
                "2025-07-20"
            ),
            NoSocio(
                5,
                "34567890",
                "Andrés",
                "Rojas",
                "1156789012",
                "Salta 555",
                "andres.r@email.com",
                true,
                "2025-08-05"
            )
        )

        for (noSocio in noSocios) {
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
            db.insert("no_socios", null, values)
        }
    }

    // ----------------------------------------------------
    // MÉTODO GENERAR CUOTAS MODIFICADO PARA FUNCIONAR EN ONCREATE
    // ----------------------------------------------------
    // Este método usa la referencia a 'db' que le pasamos desde onCreate.
    private fun generarCuotasAutomaticas(db: SQLiteDatabase, socioId: Int, fechaAlta: String) {
        val fecha = LocalDate.parse(fechaAlta)
        val hoy = LocalDate.now()

        var fechaIteracion = LocalDate.of(fecha.year, fecha.month, 1)
        val fin = LocalDate.of(hoy.year, hoy.month, 1).plusMonths(1)

        while (!fechaIteracion.isAfter(fin)) {
            val mes = fechaIteracion.monthValue
            val anio = fechaIteracion.year

            val values = ContentValues().apply {
                put("socio_id", socioId)
                put("mes", mes)
                put("anio", anio)
                put("pagado", 0)
            }
            db.insert("cuotas", null, values)

            fechaIteracion = fechaIteracion.plusMonths(1)
        }
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
                    foto = fotoBlob, // ByteArray? or null
                    fechaAlta = it.getString(it.getColumnIndexOrThrow("fecha_alta"))
                )
            }
        }
        return socio
    }

    // Obtener todos los socios
    fun obtenerTodosLosSocios(): List<Socio> {
        val lista = mutableListOf<Socio>()
        val db = readableDatabase

        val query = "SELECT * FROM socios ORDER BY apellido ASC"
        val cursor = db.rawQuery(query, null)

        cursor.use {
            if (cursor.moveToFirst()) {

                // Obtener índices de columnas (más eficiente)
                val idxId = cursor.getColumnIndexOrThrow("id_socio")
                val idxDni = cursor.getColumnIndexOrThrow("dni")
                val idxNombre = cursor.getColumnIndexOrThrow("nombre")
                val idxApellido = cursor.getColumnIndexOrThrow("apellido")
                val idxTelefono = cursor.getColumnIndexOrThrow("telefono")
                val idxDireccion = cursor.getColumnIndexOrThrow("direccion")
                val idxEmail = cursor.getColumnIndexOrThrow("email")
                val idxTipoPlan = cursor.getColumnIndexOrThrow("tipo_plan")
                val idxAptoFisico = cursor.getColumnIndexOrThrow("apto_fisico")
                val idxFoto = cursor.getColumnIndexOrThrow("foto")
                val idxFechaAlta = cursor.getColumnIndexOrThrow("fecha_alta")

                do {
                    val fotoBlob: ByteArray? =
                        if (cursor.isNull(idxFoto)) null
                        else cursor.getBlob(idxFoto)

                    val socio = Socio(
                        id = cursor.getInt(idxId),
                        dni = cursor.getString(idxDni),
                        nombre = cursor.getString(idxNombre),
                        apellido = cursor.getString(idxApellido),
                        telefono = cursor.getString(idxTelefono),
                        direccion = cursor.getString(idxDireccion),
                        email = cursor.getString(idxEmail),
                        tipoPlan = cursor.getString(idxTipoPlan),
                        aptoFisico = cursor.getInt(idxAptoFisico) == 1,
                        foto = fotoBlob,
                        fechaAlta = cursor.getString(idxFechaAlta)
                    )

                    lista.add(socio)

                } while (cursor.moveToNext())
            }
        }
        return lista
    }

    // Buscar socios por DNI
    fun buscarSocioPorDni(parcial: String): List<Socio> {
        val lista = mutableListOf<Socio>()
        val db = readableDatabase

        val query = "SELECT * FROM socios WHERE dni LIKE ? ORDER BY apellido ASC"
        val cursor = db.rawQuery(query, arrayOf("%$parcial%"))

        cursor.use {
            if (cursor.moveToFirst()) {

                val idxId = cursor.getColumnIndexOrThrow("id_socio")
                val idxDni = cursor.getColumnIndexOrThrow("dni")
                val idxNombre = cursor.getColumnIndexOrThrow("nombre")
                val idxApellido = cursor.getColumnIndexOrThrow("apellido")
                val idxTelefono = cursor.getColumnIndexOrThrow("telefono")
                val idxDireccion = cursor.getColumnIndexOrThrow("direccion")
                val idxEmail = cursor.getColumnIndexOrThrow("email")
                val idxTipoPlan = cursor.getColumnIndexOrThrow("tipo_plan")
                val idxAptoFisico = cursor.getColumnIndexOrThrow("apto_fisico")
                val idxFoto = cursor.getColumnIndexOrThrow("foto")
                val idxFechaAlta = cursor.getColumnIndexOrThrow("fecha_alta")

                do {
                    val fotoBlob: ByteArray? =
                        if (cursor.isNull(idxFoto)) null
                        else cursor.getBlob(idxFoto)

                    val socio = Socio(
                        id = cursor.getInt(idxId),
                        dni = cursor.getString(idxDni),
                        nombre = cursor.getString(idxNombre),
                        apellido = cursor.getString(idxApellido),
                        telefono = cursor.getString(idxTelefono),
                        direccion = cursor.getString(idxDireccion),
                        email = cursor.getString(idxEmail),
                        tipoPlan = cursor.getString(idxTipoPlan),
                        aptoFisico = cursor.getInt(idxAptoFisico) == 1,
                        foto = fotoBlob,
                        fechaAlta = cursor.getString(idxFechaAlta)
                    )

                    lista.add(socio)

                } while (cursor.moveToNext())
            }
        }
        return lista
    }

    // Obtener No Socio por ID
    fun obtenerNoSocioPorId(id: Int): NoSocio? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM no_socios WHERE id_no_socio = ?",
            arrayOf(id.toString())
        )

        var noSocio: NoSocio? = null

        cursor.use {
            if (it.moveToFirst()) {
                noSocio = NoSocio(
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
            }
        }
        return noSocio
    }
            }

    // Buscar No Socio por DNI
    fun buscarNoSocioPorDni(parcial: String): List<NoSocio> {
        val lista = mutableListOf<NoSocio>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM no_socios WHERE dni LIKE ? ORDER BY apellido ASC",
            arrayOf("%$parcial%")
        )

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

    // Obtener todos los No Socios
    fun obtenerTodosLosNoSocios(): List<NoSocio> {
        val lista = mutableListOf<NoSocio>()
        val db = readableDatabase

        val cursor = db.rawQuery("SELECT * FROM no_socios ORDER BY apellido ASC", null)

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

    // Generar Cuotas
    fun generarCuotasAutomaticas(socioId: Int, fechaAlta: String) {
        val db = writableDatabase

        val fecha = LocalDate.parse(fechaAlta)
        val hoy = LocalDate.now()

        var fechaIteracion = LocalDate.of(fecha.year, fecha.month, 1)
        val fin = LocalDate.of(hoy.year, hoy.month, 1).plusMonths(1)

        while (!fechaIteracion.isAfter(fin)) {
            val mes = fechaIteracion.monthValue
            val anio = fechaIteracion.year

            val values = ContentValues().apply {
                put("socio_id", socioId)
                put("mes", mes)
                put("anio", anio)
                put("pagado", 0)
            }
            db.insert("cuotas", null, values)

            fechaIteracion = fechaIteracion.plusMonths(1)
        }
        return db.insert("pagos", null, values)
    }

    //Obtener Cuotas de un Socio
    fun obtenerCuotasSocio(socioId: Int): List<Cuotas> {
        val db = readableDatabase
        val lista = mutableListOf<Cuotas>()

        val cursor = db.rawQuery(
            "SELECT * FROM cuotas WHERE socio_id=? ORDER BY anio DESC, mes DESC",
            arrayOf(socioId.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Cuotas(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        socioId = socioId,
                        mes = cursor.getInt(cursor.getColumnIndexOrThrow("mes")),
                        anio = cursor.getInt(cursor.getColumnIndexOrThrow("anio")),
                        pagado = cursor.getInt(cursor.getColumnIndexOrThrow("pagado"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
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

    // Marcar Cuota como Pagada
    fun marcarPagada(idCuota: Int) {
        val db = writableDatabase
        val values = ContentValues().apply { put("pagado", 1) }
        db.update("cuotas", values, "id=?", arrayOf(idCuota.toString()))
    }
}