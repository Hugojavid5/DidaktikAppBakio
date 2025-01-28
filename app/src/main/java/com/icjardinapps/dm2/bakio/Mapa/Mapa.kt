package com.icjardinapps.dm2.bakio.Mapa

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.icjardinapps.dm2.bakio.databinding.ActivityMapaBinding
import com.icjardinapps.dm2.bakio.*
import com.icjardinapps.dm2.bakio.Arrastrar.ActividadBienvenidaArrastrarYSoltar
import com.icjardinapps.dm2.bakio.Bela.ActividadBienvenidaBela
import com.icjardinapps.dm2.bakio.Gaztelugatze.ActividadBienvenidaGaztelugatxeko
import com.icjardinapps.dm2.bakio.Kahoot.ActividadBienvenidaKahoot
import com.icjardinapps.dm2.bakio.SanPelaio.ActividadBienvenidaSanPelaio
import com.icjardinapps.dm2.bakio.Sopa.ActividadBienvenidaSopa
import com.icjardinapps.dm2.bakio.Txakolina.ActividadBienvenidaTxakolina
import com.icjardinapps.dm2.bakio.Wally.ActividadBienvenidaWally

class Mapa : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapaBinding

    // Lista de puntos a mostrar en el mapa
    private val ciudades: MutableList<Punto> = mutableListOf(
        Punto(0, "Jaiak-Lanper Pertsonaia (Bakioko udaletxea)", "Descripción corta de Jaiak-Lanper Pertsonaia", 43.42306, -2.81417, "Descripción completa de Jaiak-Lanper Pertsonaia (Bakioko udaletxea)"),
        Punto(1, "Txakolina (Txakolingunea)", "Descripción corta de Txakolina", 43.41667, -2.81250, "Descripción completa de Txakolina (Txakolingunea)"),
        Punto(2, "Anarru eguna", "Descripción corta de Anarru eguna", 43.42944, -2.81194, "Descripción completa de Anarru eguna"),
        Punto(3, "Bela gurutzatuak haizearen kontra", "Descripción corta de Bela gurutzatuak haizearen kontra", 43.42972, -2.81056, "Descripción completa de Bela gurutzatuak haizearen kontra"),
        Punto(4, "San Pelaio ermita", "Descripción corta de San Pelaio ermita", 43.43389, -2.78556, "Descripción completa de San Pelaio ermita"),
        Punto(5, "Gaztelugatxeko doniene (San Juan)", "Descripción corta de Gaztelugatxeko doniene", 43.44700, -2.78500, "Descripción completa de Gaztelugatxeko doniene (San Juan)"),
        Punto(6, "Matxitxako itsasargia", "Descripción corta de Matxitxako itsasargia", 43.45472, -2.75250, "Descripción completa de Matxitxako itsasargia")
    )

    private var puntoActual = -1 // Controla el punto que debe estar en verde

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtenemos si debemos actualizar los puntos
        val actualizarPuntos = intent.getBooleanExtra("ACTUALIZAR_PUNTOS", false)

        // Si se debe actualizar los puntos, incrementamos el punto actual
        if (actualizarPuntos) {
            puntoActual++
        }

        // Configuramos el mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Configuramos el botón de actividad final
        binding.btnPuzzleFinal.setOnClickListener {
            startActivity(Intent(this, ActividadBienvenidaKahoot::class.java))
        }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mostrarPuntos()  // Llamamos a mostrarPuntos() solo después de que el mapa esté listo

        mMap.setOnMarkerClickListener { marker ->
            // Buscar el punto correspondiente al marcador
            val puntoSeleccionado = ciudades.find { it.nombre == marker.title }

            // Verificar si el punto seleccionado es el punto verde (el actual)
            if (puntoSeleccionado != null && puntoSeleccionado.id == puntoActual) {
                // Si es el punto verde, abrir la actividad
                abrirActividadPorId(puntoSeleccionado.id)
            } else {
                // Si no es el punto verde, no hacer nada o mostrar un mensaje
                // Opcional: Mostrar un mensaje al usuario
                Toast.makeText(this, "Debes llegar al punto actual para continuar", Toast.LENGTH_SHORT).show()
            }

            // Devolver true para indicar que el marcador ha sido procesado
            true
        }
    }

    private fun mostrarPuntos() {
        if (puntoActual < ciudades.size) {
            puntoActual++
            mMap.clear() // Limpiamos el mapa antes de mostrar los nuevos puntos

            // Mostrar los puntos en el mapa
            val boundsBuilder = LatLngBounds.Builder() // Construye los límites para ajustar la cámara

            for ((index, punto) in ciudades.withIndex()) {
                val color = when {
                    index == puntoActual -> BitmapDescriptorFactory.HUE_GREEN // Punto actual
                    else -> BitmapDescriptorFactory.HUE_RED // Puntos restantes
                }

                val latLng = LatLng(punto.latitud, punto.longitud)

                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(punto.nombre)
                        .icon(BitmapDescriptorFactory.defaultMarker(color))
                )

                boundsBuilder.include(latLng) // Agrega el punto al LatLngBounds
            }

            // Ajusta la cámara para que todos los puntos sean visibles
            val bounds = boundsBuilder.build()
            val padding = 100 // Espaciado de los bordes (opcional)
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
        } else {
            finalizarRuta()
        }
    }

    private fun abrirActividadPorId(id: Int) {
        val intent = when (id) {
            0 -> Intent(this, ActividadBienvenidaSopa::class.java)
            1 -> Intent(this, ActividadBienvenidaTxakolina::class.java)
            2 -> Intent(this, ActividadBienvenidaArrastrarYSoltar::class.java)
            3 -> Intent(this, ActividadBienvenidaBela::class.java)
            4 -> Intent(this, ActividadBienvenidaSanPelaio::class.java)
            5 -> Intent(this, ActividadBienvenidaGaztelugatxeko::class.java)
            6 -> Intent(this, ActividadBienvenidaWally::class.java)
            else -> null
        }
        intent?.let {
            startActivityForResult(it, id) // Abrimos la actividad y esperamos un resultado
        }
    }

    private fun finalizarRuta() {
        startActivity(Intent(this, ActividadBienvenidaKahoot::class.java))
        finish()
    }
}
