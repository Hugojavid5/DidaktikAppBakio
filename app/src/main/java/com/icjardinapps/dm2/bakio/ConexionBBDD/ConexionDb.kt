package com.icjardinapps.dm2.bakio.ConexionBBDD

import android.content.Context
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.Properties

class ConexionDb(context: Context) {
    private val dbUrl: String
    private val dbUser: String
    private val dbPassword: String

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

    fun ranking(): MutableList<String> {
        val lista: MutableList<String> = mutableListOf()
        val conexion = obtenerConexion()

        if (conexion != null) {
            try {
                val query = "SELECT alumno_usuario, nivel FROM puntuacion where aplicacion_id_aplicacion=3 ORDER BY nivel desc"
                val statement: PreparedStatement = conexion.prepareStatement(query)
                val resultSet: ResultSet = statement.executeQuery()

                while (resultSet.next()) {
                    val alumno = resultSet.getString("alumno_usuario")
                    val nivel = resultSet.getInt("nivel")
                    lista.add("$alumno ----------------- $nivel")
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
