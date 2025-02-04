package com.icjardinapps.dm2.bakio.Wally
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R

class ActividadBienvenidaWally : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var playButton: ImageButton
    private lateinit var pauseButton: ImageButton
    private lateinit var restartButton: ImageButton
    private lateinit var jugarButton: ImageButton
    private lateinit var currentTimeText: TextView
    private lateinit var totalTimeText: TextView
    private val handler = Handler(Looper.getMainLooper())  // Handler para actualizar la UI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida_wally)

        // Inicializar el MediaPlayer con el audio
        mediaPlayer = MediaPlayer.create(this, R.raw.wally)  // Cambia el archivo de audio
        seekBar = findViewById(R.id.seekBar)
        playButton = findViewById(R.id.btn_play)
        pauseButton = findViewById(R.id.btn_pause)
        restartButton = findViewById(R.id.btn_reiniciar)
        jugarButton = findViewById(R.id.btnJugar)
        currentTimeText = findViewById(R.id.txtCurrentTime)
        totalTimeText = findViewById(R.id.txtTotalTime)

        // Configuración inicial de la barra de progreso
        seekBar.max = mediaPlayer.duration
        seekBar.progress = 0

        // Establecer el tiempo total
        totalTimeText.text = formatTime(mediaPlayer.duration)

        // Actualizar la barra de progreso mientras se reproduce el audio
        mediaPlayer.setOnPreparedListener {
            updateSeekBar()
        }

        // Control de botones
        playButton.setOnClickListener {
            mediaPlayer.start()
            updateSeekBar()  // Iniciar actualización del SeekBar al reproducir el audio
        }

        pauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }

        restartButton.setOnClickListener {
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
            updateSeekBar()  // Reiniciar actualización del SeekBar
        }

        jugarButton.setOnClickListener {
            mediaPlayer.pause()  // Pausar el audio al ir a la siguiente actividad
            val intent = Intent(this, ActividadWally::class.java)  // Cambia JuegoActivity por tu actividad de juego
            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.release()
    }

    private fun updateSeekBar() {
        // Actualiza el SeekBar y el tiempo actual
        val runnable = object : Runnable {
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    seekBar.progress = mediaPlayer.currentPosition
                    currentTimeText.text = formatTime(mediaPlayer.currentPosition)
                    handler.postDelayed(this, 1000)  // Actualizar cada segundo
                }
            }
        }
        handler.post(runnable)
    }

    private fun formatTime(milliseconds: Int): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / 1000) / 60
        return String.format("%02d:%02d", minutes, seconds)
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // No hacemos nada, por lo que no se realizará ninguna acción al presionar la flecha de retroceso
    }
}
