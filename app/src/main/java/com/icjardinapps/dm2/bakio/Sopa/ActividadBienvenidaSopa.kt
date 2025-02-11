package com.icjardinapps.dm2.bakio.Sopa

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R
import java.util.concurrent.TimeUnit

/**
 * Actividad que muestra una pantalla de bienvenida con controles de audio y permite navegar a otro ejercicio.
 * El usuario puede reproducir, pausar, reiniciar la música y ver el progreso del tiempo.
 */
class ActividadBienvenidaSopa : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var txtCurrentTime: TextView
    private lateinit var txtTotalTime: TextView
    private val handler = Handler()
    /**
     * Metodo que se llama cuando se crea la actividad.
     * Inicializa los controles, el reproductor de audio y configura las interacciones con los botones y el SeekBar.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida_sopa)

        // Inicializar MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.bela)

        // Referencias a los controles
        seekBar = findViewById(R.id.seekBar)
        txtCurrentTime = findViewById(R.id.txtCurrentTime)
        txtTotalTime = findViewById(R.id.txtTotalTime)
        val btnPlay: ImageButton = findViewById(R.id.btn_play)
        val btnPause: ImageButton = findViewById(R.id.btn_pause)
        val btnRestart: ImageButton = findViewById(R.id.btn_reiniciar)
        val btnJugar: ImageButton = findViewById(R.id.btnJugar)

        // Configurar la barra de progreso y el tiempo total
        seekBar.max = mediaPlayer.duration
        txtTotalTime.text = formatTime(mediaPlayer.duration)

        // Listener del SeekBar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                    txtCurrentTime.text = formatTime(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Botón Play
        btnPlay.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                updateSeekBar()
            }
        }

        // Botón Pause
        btnPause.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }

        // Botón Restart
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
            val intent = Intent(this, ActividadSopaDeLetras::class.java)
            startActivity(intent)
        }
    }

     /**
     * Actualiza el SeekBar y el tiempo actual mientras la música se reproduce.
     * Este metodo se ejecuta periódicamente para reflejar la posición actual de la canción.
     */
    private fun updateSeekBar() {
        val runnable = object : Runnable {
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    seekBar.progress = mediaPlayer.currentPosition
                    txtCurrentTime.text = formatTime(mediaPlayer.currentPosition)
                    handler.postDelayed(this, 500)
                }
            }
        }
        handler.post(runnable)
    }

    /**
     * Formatea el tiempo en milisegundos a un formato mm:ss.
     *
     * @param ms El tiempo en milisegundos a formatear.
     * @return El tiempo formateado en el formato mm:ss.
     */
    private fun formatTime(ms: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(ms.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(ms.toLong()) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
    /**
     * Libera los recursos del MediaPlayer cuando la actividad es destruida.
     * Detiene la reproducción si está en curso y elimina el handler para evitar filtraciones de memoria.
     */
    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }
    /**
     * Override del método onBackPressed para evitar la acción predeterminada de retroceder al presionar la flecha de retroceso.
     *
     * Este método impide que la actividad se cierre cuando se presiona el botón de retroceso.
     */
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // No hacemos nada, por lo que no se realizará ninguna acción al presionar la flecha de retroceso
    }
}
