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

class ActividadBienvenidaBela : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var currentTimeText: TextView
    private lateinit var totalTimeText: TextView

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

    // Metodo para formatear el tiempo en formato "mm:ss"
    private fun formatTime(millis: Int): String {
        val minutes = millis / 1000 / 60
        val seconds = (millis / 1000 % 60)
        return String.format("%02d:%02d", minutes, seconds)
    }

    // Actualizar SeekBar y el tiempo actual mientras se reproduce el audio
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

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // No hacemos nada, por lo que no se realizará ninguna acción al presionar la flecha de retroceso
    }
}
