package com.icjardinapps.dm2.bakio.Mapa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.icjardinapps.dm2.bakio.Arrastrar.ActividadArrastrarYSoltar
import com.icjardinapps.dm2.bakio.Bela.ActividadBienvenidaBela
import com.icjardinapps.dm2.bakio.Gaztelugatze.ActividadBienvenidaGaztelugatxeko
import com.icjardinapps.dm2.bakio.Kahoot.ActividadBienvenidaKahoot
import com.icjardinapps.dm2.bakio.R
import com.icjardinapps.dm2.bakio.SanPelaio.ActividadBienvenidaSanPelaio
import com.icjardinapps.dm2.bakio.Txakolina.ActividadBienvenidaTxakolina
import com.icjardinapps.dm2.bakio.Wally.ActividadBienvenidaWally
import com.icjardinapps.dm2.bakio.databinding.ActivityMapaBinding

class Mapa : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapaBinding

    // Lista de puntos a mostrar en el mapa
    private val ciudades: List<Punto> = listOf(
        Punto(0, "Jaiak-Lanper Pertsonaia (Bakioko udaletxea)", "Descripción corta de Jaiak-Lanper Pertsonaia", 43.42306, -2.81417, "Descripción completa de Jaiak-Lanper Pertsonaia (Bakioko udaletxea)"),
        Punto(1, "Txakolina (Txakolingunea)", "Descripción corta de Txakolina", 43.41667, -2.81250, "Descripción completa de Txakolina (Txakolingunea)"),
        Punto(2, "Anarru eguna", "Descripción corta de Anarru eguna", 43.42944, -2.81194, "Descripción completa de Anarru eguna"),
        Punto(3, "Bela gurutzatuak haizearen kontra", "Descripción corta de Bela gurutzatuak haizearen kontra", 43.42972, -2.81056, "Descripción completa de Bela gurutzatuak haizearen kontra"),
        Punto(4, "San Pelaio ermita", "Descripción corta de San Pelaio ermita", 43.43389, -2.78556, "Descripción completa de San Pelaio ermita"),
        Punto(5, "Gaztelugatxeko doniene (San Juan)", "Descripción corta de Gaztelugatxeko doniene", 43.44700, -2.78500, "Descripción completa de Gaztelugatxeko doniene (San Juan)"),
        Punto(6, "Matxitxako itsasargia", "Descripción corta de Matxitxako itsasargia", 43.45472, -2.75250, "Descripción completa de Matxitxako itsasargia")
    )

    private var currentPointIndex = 0 // Índice del punto actual

    private lateinit var actividadLauncher: ActivityResultLauncher<Intent> // Launcher para actividades

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar el launcher
        actividadLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Si la actividad fue completada correctamente, avanzamos al siguiente punto
                avanzarAlSiguientePunto()
            } else {
                // Si no fue completado correctamente, mostramos un mensaje
                Toast.makeText(this, "Actividad no completada correctamente.", Toast.LENGTH_SHORT).show()
            }
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Mostrar todos los puntos en el mapa
        mostrarTodosLosPuntos()

        // Agregar un listener para abrir la actividad al pulsar un marcador
        mMap.setOnMarkerClickListener { marker ->
            // Buscar el punto asociado al marcador pulsado
            val puntoSeleccionado = ciudades.find { it.nombre == marker.title }
            puntoSeleccionado?.let { abrirActividadPorId(it.id) }
            true // Indica que el evento ha sido gestionado
        }
    }

    private fun mostrarTodosLosPuntos() {
        mMap.clear() // Limpiar los marcadores anteriores

        // Crear un objeto LatLngBounds para ajustar la cámara y mostrar todos los puntos
        val boundsBuilder = LatLngBounds.Builder()

        // Agregar marcadores para todos los puntos
        for (punto in ciudades) {
            val posicion = LatLng(punto.latitud, punto.longitud)
            mMap.addMarker(
                MarkerOptions()
                    .position(posicion)
                    .title(punto.nombre)
            )
            boundsBuilder.include(posicion) // Agregar posición al bounds
        }

        // Ajustar la cámara para mostrar todos los puntos
        val bounds = boundsBuilder.build()
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    private fun abrirActividadPorId(id: Int) {
        val intent = when (id) {
            0 -> Intent(this, ActividadBienvenidaWally::class.java)
            1 -> Intent(this, ActividadBienvenidaTxakolina::class.java)
            2 -> Intent(this, ActividadArrastrarYSoltar::class.java)
            3 -> Intent(this, ActividadBienvenidaBela::class.java)
            4 -> Intent(this, ActividadBienvenidaSanPelaio::class.java)
            5 -> Intent(this, ActividadBienvenidaGaztelugatxeko::class.java)
            6 -> Intent(this, ActividadBienvenidaWally::class.java)
            else -> null
        }
        intent?.let {
            actividadLauncher.launch(it) // Llamamos a la actividad y esperamos un resultado
        }
    }

    private fun avanzarAlSiguientePunto() {
        // Asegurarse de que el índice no se desborde
        if (currentPointIndex < ciudades.size - 1) {
            currentPointIndex++ // Avanzar al siguiente punto
            mostrarTodosLosPuntos() // Mostrar el siguiente punto
        } else {
            // Si ya se completaron todos los puntos, finalizar la ruta
            finalizarRuta()
        }
    }

    //Cuando se acaban todos los puntos abre una actividad extra
    private fun finalizarRuta() {
        startActivity(Intent(this, ActividadBienvenidaKahoot::class.java))
        finish()
    }
}
