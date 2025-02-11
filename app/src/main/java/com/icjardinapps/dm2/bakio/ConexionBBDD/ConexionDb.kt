package com.icjardinapps.dm2.bakio.ConexionBBDD

import android.content.Context
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.Properties
/**
 * Clase que gestiona la conexión con la base de datos y las operaciones relacionadas.
 */
class ConexionDb(context: Context) {

    private val dbUrl: String
    private val dbUser: String
    private val dbPassword: String
/**
     * Constructor que carga las credenciales de la base de datos desde el archivo `config.properties`.
     *
     * @param context Contexto de la aplicación para acceder a los assets.
     */
    init {
        val properties = Properties()
        try {
            val inputStream = context.assets.open("config.properties")
            properties.load(inputStream)
            inputStream.close()

            dbUrl = properties.getProperty("dburl")
                ?: throw IllegalArgumentException("La propiedad dburl no está definida en config.properties")
            dbUser = properties.getProperty("user")
                ?: throw IllegalArgumentException("La propiedad user no está definida en config.properties")
            dbPassword = properties.getProperty("pass")
                ?: throw IllegalArgumentException("La propiedad pass no está definida en config.properties")
        } catch (e: Exception) {
            Log.e("CONFIG_ERROR", "Error al cargar config.properties: ${e.message}")
            throw e
        }
    }

/**
     * Establece una conexión con la base de datos.
     *
     * @return Un objeto `Connection` si la conexión es exitosa, o `null` en caso de error.
     */
    fun obtenerConexion(): Connection? {
        return try {
            val conexion = DriverManager.getConnection(dbUrl, dbUser, dbPassword)
            Log.i("DB_INFO", "Conexión a la base de datos establecida correctamente.")
            conexion
        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Error al conectar a la base de datos: ${e.message}")
            null
        }
    }

    /**
     * Guarda un nuevo alumno en la base de datos.
     *
     * @param usuario Nombre de usuario del alumno.
     * @param nombre  Nombre completo del alumno.
     * @param idAplicacion Identificador de la aplicación.
     * @return `true` si la operación fue exitosa, `false` en caso de error.
     */
    fun guardarAlumnoBBDD(usuario: String, nombre: String, idAplicacion: Int): Boolean {
        val conexion = obtenerConexion() ?: return false

        var statement: PreparedStatement? = null
        return try {
            val query = "INSERT INTO alumno (usuario, nombre, año_nacimiento, id_aplicacion) VALUES (?, ?, ?, ?)"
            statement = conexion.prepareStatement(query)
            statement.setString(1, usuario)         // usuario
            statement.setString(2, nombre)          // nombre
            statement.setInt(3, 2000)               // año_nacimiento
            statement.setInt(4, idAplicacion)       // id_aplicacion
            statement.executeUpdate()

            Log.i("DB_INFO", " Usuario $usuario registrado correctamente en la base de datos.")
            true
        } catch (e: SQLException) {
            Log.e("SQL_ERROR", " Error al guardar alumno: ${e.message}")
            false
        } finally {
            try {
                statement?.close()
                conexion.close()
                Log.i("DB_INFO", " Conexión cerrada correctamente en guardarAlumnoBBDD().")
            } catch (e: SQLException) {
                Log.e("DB_ERROR", " Error al cerrar la conexión: ${e.message}")
            }
        }
    }


    /**
     * Guarda la puntuación de un alumno en un nivel de la aplicación.
     *
     * @param usuario Nombre de usuario del alumno.
     * @param puntuacion Puntuación obtenida en el nivel.
     * @return `true` si la operación fue exitosa, `false` en caso de error.
     */
    fun guardarPuntuacionNivel(usuario:String,puntuacion:Int):Boolean{
        val conexion = obtenerConexion()
        if (conexion != null) {
            try {
                val query =
                    "INSERT INTO puntuacion (alumno_usuario,aplicacion_id_aplicacion,nivel) VALUES (?,?,?)"
                val statement: PreparedStatement = conexion.prepareStatement(query)
                statement.setString(1, usuario)
                statement.setInt(2,3)
                statement.setInt(3,puntuacion)
                statement.executeUpdate()
                return true
            } catch (e: SQLException) {
                e.printStackTrace()
                return false
            } finally {
                conexion.close()
            }
        }
        return false
    }

    /**
     * Obtiene un ranking de los alumnos según su puntuación en la aplicación.
     *
     * @return Una lista con los nombres de usuario y sus puntuaciones ordenadas de mayor a menor.
     */
    fun ranking(): MutableList<String> {
        val lista: MutableList<String> = mutableListOf()
        val conexion = obtenerConexion()

        if (conexion != null) {
            try {
                val query = "SELECT alumno_usuario, nivel FROM puntuacion where aplicacion_id_aplicacion=3 ORDER BY nivel desc"
                val statement: PreparedStatement = conexion.prepareStatement(query)
                val resultSet: ResultSet = statement.executeQuery()

                var posicion = 1 // Iniciar la posición en 1

                while (resultSet.next()) {
                    val alumno = resultSet.getString("alumno_usuario").toUpperCase() // Convertir el nombre a mayúsculas
                    val nivel = resultSet.getInt("nivel")

                    // Crear el string con el formato requerido
                    lista.add("$posicion. $alumno : $nivel puntos")

                    posicion++ // Aumentar la posición para la siguiente iteración
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                conexion.close()
            }
        }
        return lista
    }




}
