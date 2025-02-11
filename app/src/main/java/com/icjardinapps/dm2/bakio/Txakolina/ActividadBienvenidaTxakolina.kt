package com.icjardinapps.dm2.bakio.Txakolina

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R

/**
 * Actividad de bienvenida para la sección de "Txakolina", donde se controla la reproducción
 * de un archivo de audio y la interacción con los botones para reproducir, pausar, reiniciar
 * el audio, y comenzar el juego. Además, maneja la barra de progreso (SeekBar) para mostrar
 * el progreso del audio y permite a los usuarios controlar la posición del audio.
 */
class ActividadBienvenidaTxakolina : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
     /**
     * Método llamado cuando se crea la actividad. Inicializa el MediaPlayer, los botones de control
     * y la barra de progreso (SeekBar) para controlar la reproducción del audio.
     * También configura las acciones de los botones de control (Play, Pause, Reiniciar, Jugar).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida_txakolina)

        // Configurar MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.txakolinaaudioa)

        // Configurar SeekBar y TextViews
        val audioSeekBar: SeekBar = findViewById(R.id.audioSeekBar)
        val currentTimeText: TextView = findViewById(R.id.currentTimeText)
        val totalTimeText: TextView = findViewById(R.id.totalTimeText)

        // Establecer el tiempo total del audio
        totalTimeText.text = formatTime(mediaPlayer.duration)

        // Configurar SeekBar
        audioSeekBar.max = mediaPlayer.duration

        // Configurar botones de control
        val playButton: ImageButton = findViewById(R.id.btn_play)
        val pauseButton: ImageButton = findViewById(R.id.btn_pause)
        val restartButton: ImageButton = findViewById(R.id.btn_reiniciar)
        val playGameButton: ImageButton = findViewById(R.id.btnJugar)

        // Acción del botón Play
        playButton.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()

                // Actualizar la barra de progreso y tiempo transcurrido
                val updateRunnable = object : Runnable {
                    override fun run() {
                        val currentPosition = mediaPlayer.currentPosition
                        audioSeekBar.progress = currentPosition
                        currentTimeText.text = formatTime(currentPosition)
                        if (mediaPlayer.isPlaying) {
                            audioSeekBar.postDelayed(this, 1000)
                        }
                    }
                }
                audioSeekBar.postDelayed(updateRunnable, 1000)
            }
        }

        // Acción del botón Pause
        pauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }

        // Acción del botón Reiniciar
        restartButton.setOnClickListener {
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
        }

        // Acción del botón Jugar
        playGameButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
            val intent = Intent(this, ActividadTxakoli::class.java)
            startActivity(intent)
        }

        // Configurar SeekBar y su Listener para adelantar el audio
        audioSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) { // Solo cambiar la posición si el usuario mueve el SeekBar
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Opcionalmente, puedes pausar el audio mientras el usuario mueve el SeekBar
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Opcionalmente, reanudar la reproducción al soltar el SeekBar
                if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                }
            }
        })
    }

    /**
     * Método para formatear el tiempo en milisegundos a formato mm:ss.
     *
     * @param milliseconds El tiempo en milisegundos a formatear.
     * @return El tiempo formateado como una cadena en el formato mm:ss.
     */
    private fun formatTime(milliseconds: Int): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / 1000) / 60
        return String.format("%02d:%02d", minutes, seconds)
    }
    /**
     * Método llamado cuando la actividad es destruida. Libera los recursos utilizados por el MediaPlayer.
     */
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
    /**
     * Sobrescribe el comportamiento de la flecha de retroceso del dispositivo para evitar que la actividad se cierre.
     * No realiza ninguna acción cuando el usuario presiona la flecha de retroceso.
     */
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // No hacemos nada, por lo que no se realizará ninguna acción al presionar la flecha de retroceso
    }
}
