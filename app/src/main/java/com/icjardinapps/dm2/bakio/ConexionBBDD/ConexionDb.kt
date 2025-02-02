package com.icjardinapps.dm2.bakio.ConexionBBDD

import android.content.Context
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.Properties

class ConexionDb(context: Context) {
    private val dbUrl:String
    private val dbUser:String
    private val dbPassword:String


    init {
        // Crear instancia de Properties
        val properties = Properties()

        // Cargar el archivo .properties
        try {
            // Usar el Context para acceder a assets
            val inputStream = context.assets.open("config.properties")
            properties.load(inputStream)
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Obtener valores de las propiedades
        dbUrl = properties.getProperty("dburl") ?: throw IllegalArgumentException("db_url no definido")
        dbUser = properties.getProperty("user") ?: throw IllegalArgumentException("db_user no definido")
        dbPassword = properties.getProperty("pass") ?: throw IllegalArgumentException("db_password no definido")
    }

    fun obtenerConexion(): Connection? {
        return try {
            DriverManager.getConnection(dbUrl, dbUser, dbPassword)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    fun guardarAlumnoBBDD(usuario: String): Boolean {
        val conexion = obtenerConexion()
        if (conexion != null) {
            try {
                val query =
                    "INSERT INTO alumno (usuario, nombre, año_nacimiento, id_aplicacion) VALUES (?, ?, ?, ?)"
                val statement: PreparedStatement = conexion.prepareStatement(query)
                statement.setString(1,usuario)
                statement.setString(2, usuario)
                statement.setInt(3, 2000)
                statement.setInt(4, 4)
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

    fun guardarPuntuacionNivel(usuario:String,puntuacion:Int,lugar:String):Boolean{
        val conexion = obtenerConexion()
        if (conexion != null) {
            try {
                val query =
                    "INSERT INTO puntuacion (usuario,id_aplicacion,nivel,lugar) VALUES (?,?,?,?)"
                val statement: PreparedStatement = conexion.prepareStatement(query)
                statement.setString(1, usuario)
                statement.setInt(2,4)
                statement.setInt(2,puntuacion)
                statement.setString(2,lugar)
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

}