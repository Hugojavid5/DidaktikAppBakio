package com.icjardinapps.dm2.bakio.Mapa

import android.annotation.SuppressLint
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

    private val ciudades: MutableList<Punto> = mutableListOf(
        Punto(0, "Jaiak-Lanper Pertsonaia (Bakioko udaletxea)", "Descripción corta", 43.42306, -2.81417, "Descripción completa"),
        Punto(1, "Txakolina (Txakolingunea)", "Descripción corta", 43.41667, -2.81250, "Descripción completa"),
        Punto(2, "Anarru eguna", "Descripción corta", 43.42944, -2.81194, "Descripción completa"),
        Punto(3, "Bela gurutzatuak haizearen kontra", "Descripción corta", 43.42972, -2.81056, "Descripción completa"),
        Punto(4, "San Pelaio ermita", "Descripción corta", 43.43389, -2.78556, "Descripción completa"),
        Punto(5, "Gaztelugatxeko doniene (San Juan)", "Descripción corta", 43.44700, -2.78500, "Descripción completa"),
        Punto(6, "Matxitxako itsasargia", "Descripción corta", 43.45472, -2.75250, "Descripción completa")
    )

    private var puntoActual = 0 // El primer punto será el actual desde el inicio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Deshabilitar el botón al principio
        binding.btnPuzzleFinal.isEnabled = false

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnPuzzleFinal.setOnClickListener {
            startActivity(Intent(this, ActividadBienvenidaKahoot::class.java))
        }
    }

    private fun mostrarPuntos() {
        mMap.clear()
        val boundsBuilder = LatLngBounds.Builder()

        for ((index, punto) in ciudades.withIndex()) {
            // Si todos los puntos están completos, todos se deben marcar en rojo
            val color = if (todosPuntosCompletados()) {
                BitmapDescriptorFactory.HUE_RED
            } else {
                when {
                    index == puntoActual && !punto.completado -> BitmapDescriptorFactory.HUE_GREEN
                    punto.completado -> BitmapDescriptorFactory.HUE_RED
                    else -> BitmapDescriptorFactory.HUE_ORANGE
                }
            }

            val latLng = LatLng(punto.latitud, punto.longitud)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(punto.nombre)
                    .icon(BitmapDescriptorFactory.defaultMarker(color))
            )

            boundsBuilder.include(latLng)
        }

        val bounds = boundsBuilder.build()
        val padding = 100
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))

        // Habilitar el botón si todos los puntos están completados
        if (todosPuntosCompletados()) {
            binding.btnPuzzleFinal.isEnabled = true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mostrarPuntos()

        mMap.setOnMarkerClickListener { marker ->
            val puntoSeleccionado = ciudades.find { it.nombre == marker.title }

            if (puntoSeleccionado != null && puntoSeleccionado.id == puntoActual && !puntoSeleccionado.completado) {
                puntoSeleccionado.completar()
                puntoActual++
                mostrarPuntos()
                abrirActividadPorId(puntoSeleccionado.id)
            } else {
                Toast.makeText(this, getString(R.string.debes_llegar_al_punto_actual_para_continuar), Toast.LENGTH_SHORT).show()
            }
            true
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
        intent?.let { startActivityForResult(it, id) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            ciudades.find { it.id == requestCode }?.completar()
            mostrarPuntos()

            // Verificar si todos los puntos han sido completados
            if (todosPuntosCompletados()) {
                Toast.makeText(this, "¡Ruta completada!", Toast.LENGTH_SHORT).show()
                // Marcar todos los puntos en rojo
                mostrarPuntos()  // Esto actualizará el mapa con los puntos en rojo
                // Regresar al mapa
                finalizarRuta()
            }
        }
    }

    // Función para verificar si todos los puntos están completados
    private fun todosPuntosCompletados(): Boolean {
        return ciudades.all { it.completado }
    }

    private fun finalizarRuta() {
        // Esto te lleva nuevamente al mapa, donde podrás visualizar los puntos.
        val intent = Intent(this, Mapa::class.java)
        startActivity(intent)
        finish()
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // No hacemos nada, por lo que no se realizará ninguna acción al presionar la flecha de retroceso
    }
}
