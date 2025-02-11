package com.icjardinapps.dm2.bakio.Bela

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.os.Handler
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R
/**
 * Actividad de bienvenida para "Bela". En esta pantalla, se reproduce un audio con controles
 * de reproducción antes de iniciar la actividad principal del juego.
 */
class ActividadBienvenidaBela : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var currentTimeText: TextView
    private lateinit var totalTimeText: TextView
    /**
     * Método que se ejecuta al crear la actividad.
     * @param savedInstanceState Estado guardado de la actividad (si existe).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida_bela)

        // Inicializar MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.bela)

        // Inicializar vistas
        seekBar = findViewById(R.id.audioSeekBar)
        currentTimeText = findViewById(R.id.currentTimeText)
        totalTimeText = findViewById(R.id.totalTimeText)

        val btnPlay: ImageButton = findViewById(R.id.btn_play)
        val btnPause: ImageButton = findViewById(R.id.btn_pause)
        val btnRestart: ImageButton = findViewById(R.id.btn_reiniciar)
        val btnJugar: ImageButton = findViewById(R.id.btnJugar)


        // Configurar SeekBar
        seekBar.max = mediaPlayer.duration // Establece el máximo del SeekBar al tiempo total del audio
        totalTimeText.text = formatTime(mediaPlayer.duration) // Muestra el tiempo total en formato adecuado

        // Reproducir audio
        btnPlay.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                updateSeekBar()
            }
        }

        // Pausar audio
        btnPause.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }

        // Reiniciar audio
        btnRestart.setOnClickListener {
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
            updateSeekBar()
        }

        // Botón Jugar
        btnJugar.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
            val intent = Intent(this, ActividadBela::class.java)
            startActivity(intent)
        }

        // Actualizar SeekBar con el progreso del audio
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    /**
     * Método para formatear el tiempo en formato "mm:ss".
     * @param millis Tiempo en milisegundos.
     * @return Cadena de texto con el tiempo en formato "mm:ss".
     */

    private fun formatTime(millis: Int): String {
        val minutes = millis / 1000 / 60
        val seconds = (millis / 1000 % 60)
        return String.format("%02d:%02d", minutes, seconds)
    }

    /**
     * Método para actualizar el SeekBar y el tiempo actual del audio mientras se reproduce.
     */
    private fun updateSeekBar() {
        val handler = Handler()
        val updateRunnable = object : Runnable {
            override fun run() {
                val currentPosition = mediaPlayer.currentPosition
                seekBar.progress = currentPosition
                currentTimeText.text = formatTime(currentPosition) // Muestra el tiempo actual en formato adecuado
                handler.postDelayed(this, 1000) // Actualiza cada segundo
            }
        }
        handler.post(updateRunnable)
    }
/**
     * Libera los recursos de MediaPlayer cuando la actividad se destruye.
     */
    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
    /**
     * Deshabilita el botón de retroceso para evitar que el usuario salga accidentalmente.
     */
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // No hacemos nada, por lo que no se realizará ninguna acción al presionar la flecha de retroceso
    }
}
