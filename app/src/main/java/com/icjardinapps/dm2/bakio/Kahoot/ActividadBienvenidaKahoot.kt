package com.icjardinapps.dm2.bakio.Kahoot

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R


class ActividadBienvenidaKahoot : AppCompatActivity() {

    private lateinit var textViewTiempoIzquierda: TextView
    private lateinit var textViewTiempoDerecha: TextView
    private lateinit var seekBarAudio: SeekBar
    private lateinit var botonPlay: Button
    private lateinit var botonPause: Button
    private lateinit var botonReiniciar: Button

    private var tiempoTotal: Int = 6 // tiempo total en segundos (por ejemplo, 3 minutos)
    private var tiempoTranscurrido: Int = 0
    private val handler = Handler()

    private val actualizarTiempoRunnable = object : Runnable {
        override fun run() {
            if (tiempoTranscurrido < tiempoTotal) {
                tiempoTranscurrido++
                val minutos = tiempoTranscurrido / 60
                val segundos = tiempoTranscurrido % 60
                textViewTiempoIzquierda.text = String.format("%02d:%02d", minutos, segundos)

                seekBarAudio.progress = tiempoTranscurrido

                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida_kahoot)

        // Inicializar vistas
        textViewTiempoIzquierda = findViewById(R.id.textViewTiempoIzquierda) // Inicialización de textViewTiempoIzquierda
        textViewTiempoDerecha = findViewById(R.id.textViewTiempoDerecha) // Inicialización de textViewTiempoDerecha
        seekBarAudio = findViewById(R.id.seekBarAudio)
        botonPlay = findViewById(R.id.botonPlay)
        botonPause = findViewById(R.id.botonPause)
        botonReiniciar = findViewById(R.id.botonReiniciar)

        // Configurar SeekBar
        seekBarAudio.max = tiempoTotal
        seekBarAudio.progress = tiempoTranscurrido

        // Configuración inicial de tiempo total en el TextView de la derecha
        val minutosTotal = tiempoTotal / 60
        val segundosTotal = tiempoTotal % 60
        textViewTiempoDerecha.text = String.format("%02d:%02d", minutosTotal, segundosTotal)

        // Acción del botón Play
        botonPlay.setOnClickListener {
            botonPlay.isEnabled = false
            botonPause.isEnabled = true
            handler.post(actualizarTiempoRunnable)
        }

        // Acción del botón Pause
        botonPause.setOnClickListener {
            botonPlay.isEnabled = true
            botonPause.isEnabled = false
            handler.removeCallbacks(actualizarTiempoRunnable)
        }

        // Acción del botón Reiniciar
        botonReiniciar.setOnClickListener {
            botonPlay.isEnabled = true
            botonPause.isEnabled = false
            handler.removeCallbacks(actualizarTiempoRunnable)
            tiempoTranscurrido = 0
            seekBarAudio.progress = tiempoTranscurrido
            textViewTiempoIzquierda.text = "00:00"
        }

        // Sincronización de la SeekBar con el tiempo
        seekBarAudio.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    tiempoTranscurrido = progress
                    val minutos = tiempoTranscurrido / 60
                    val segundos = tiempoTranscurrido % 60
                    textViewTiempoIzquierda.text = String.format("%02d:%02d", minutos, segundos)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(actualizarTiempoRunnable)
    }
}
