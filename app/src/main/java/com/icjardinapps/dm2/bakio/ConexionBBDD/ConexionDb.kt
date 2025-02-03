package com.icjardinapps.dm2.bakio.ConexionBBDD

import android.content.Context
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
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
        Log.i("INFO", "ENTRADO EN BASE DE DATOS")
        return try {
            DriverManager.getConnection(dbUrl, dbUser, dbPassword)
        } catch (e: SQLException) {
            Log.e("DB_ERROR", "Fallo de base de datos: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    fun guardarAlumnoBBDD(usuario: String, nombre: String, idAplicacion: Int): Boolean {
        val conexion = obtenerConexion()
        if (conexion != null) {
            try {
                val query = "INSERT INTO alumno (usuario, nombre, año_nacimiento, id_aplicacion) VALUES (?, ?, ?, ?)"
                val statement: PreparedStatement = conexion.prepareStatement(query)
                statement.setString(1, usuario)         // usuario
                statement.setString(2, nombre)          // nombre
                statement.setInt(3, 2000)               // año_nacimiento
                statement.setInt(4, idAplicacion)       // id_aplicacion
                statement.executeUpdate()

                return true
            } catch (e: SQLException) {
                Log.e("SQL_ERROR", "Error al guardar alumno: ${e.message}")
                e.printStackTrace()
                return false
            } finally {
                conexion.close()
            }
        }
        return false
    }


    fun guardarPuntuacionNivel(usuario: String, puntuacion: Int, lugar: String): Boolean {
        val conexion = obtenerConexion()
        if (conexion != null) {
            try {
                val query = "INSERT INTO puntuacion (usuario, id_aplicacion, nivel, lugar) VALUES (?, ?, ?, ?)"
                val statement: PreparedStatement = conexion.prepareStatement(query)
                statement.setString(1, usuario)
                statement.setInt(2, 4)
                statement.setInt(3, puntuacion)
                statement.setString(4, lugar)
                statement.executeUpdate()
                return true
            } catch (e: SQLException) {
                Log.e("SQL_ERROR", "Error al guardar puntuación: ${e.message}")
                e.printStackTrace()
                return false
            } finally {
                conexion.close()
            }
        }
        return false
    }
}
