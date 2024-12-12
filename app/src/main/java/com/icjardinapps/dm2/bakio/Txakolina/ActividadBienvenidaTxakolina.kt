package com.icjardinapps.dm2.bakio.Txakolina

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R

class ActividadBienvenidaTxakolina : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

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

        // Asegurarse de que el MediaPlayer esté preparado antes de interactuar con él
        mediaPlayer.setOnPreparedListener {
            // Iniciar reproducción
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

        // Configurar botones
        val playButton: Button = findViewById(R.id.playButton)
        val pauseButton: Button = findViewById(R.id.pauseButton)
        val restartButton: Button = findViewById(R.id.restartButton)
        val playGameButton: Button = findViewById(R.id.playGameButton)

        // Acción del botón Play
        playButton.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
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

    // Metodo para formatear el tiempo a formato mm:ss
    private fun formatTime(milliseconds: Int): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / 1000) / 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
