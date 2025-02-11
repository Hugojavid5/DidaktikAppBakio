package com.icjardinapps.dm2.bakio.Mapa
/**
 * Clase que representa un punto de interés en el mapa.
 * Cada punto tiene información sobre su nombre, descripción, coordenadas geográficas y un estado que indica si ha sido completado.
 *
 * @property id El identificador único del punto.
 * @property nombre El nombre del punto de interés.
 * @property descripcionCorta Una descripción breve del punto.
 * @property latitud La latitud del punto de interés.
 * @property longitud La longitud del punto de interés.
 * @property descripcion Una descripción completa del punto.
 * @property completado El estado del punto, indica si el punto ha sido completado o no. Por defecto es `false`.
 */
data class Punto(
    val id: Int,
    val nombre: String,
    val descripcionCorta: String,
    val latitud: Double,
    val longitud: Double,
    val descripcion: String,
    var completado: Boolean = false // Agregamos un estado para saber si el punto está completado o no
) {
    /**
     * Método para marcar el punto como completado.
     * Cambia el estado del punto a `true`, indicando que el usuario ha completado la actividad asociada.
     */
    fun completar() {
        completado = true
    }
}


