package com.icjardinapps.dm2.bakio.Mapa

data class Punto(
    val id: Int,
    val nombre: String,
    val descripcionCorta: String,
    val latitud: Double,
    val longitud: Double,
    val descripcion: String,
    var completado: Boolean = false // Agregamos un estado para saber si el punto está completado o no
) {
    // Metodo para completar un punto y cambiar su estado a completado
    fun completar() {
        completado = true
    }
}


