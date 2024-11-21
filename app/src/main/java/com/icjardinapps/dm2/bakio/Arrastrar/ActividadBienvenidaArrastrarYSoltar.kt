package com.example.bakio.arrastrar

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bakio.R

class ActividadBienvenidaArrastrarYSoltar : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida_arrastrar)

        // Inicializar el MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.anarruegunaaudioa)

        val btnPlay: Button = findViewById(R.id.btnPlay)
        val btnPause: Button = findViewById(R.id.btnPause)
        val btnRestart: Button = findViewById(R.id.btnRestart)
        val btnJugar: Button = findViewById(R.id.btnJugar)

        // Vincular la barra de progreso y los TextViews de tiempo
        val audioSeekBar: SeekBar = findViewById(R.id.audioSeekBar)
        val currentTimeText: TextView = findViewById(R.id.currentTimeText)
        val totalTimeText: TextView = findViewById(R.id.totalTimeText)

        // Obtener la duración total del audio
        val totalDuration = mediaPlayer.duration
        val minutes = totalDuration / 1000 / 60
        val seconds = totalDuration / 1000 % 60
        val totalDurationFormatted = String.format("%02d:%02d", minutes, seconds)

        // Establecer la duración total en el TextView
        totalTimeText.text = totalDurationFormatted

        // Configurar el SeekBar
        audioSeekBar.max = totalDuration

        // Reproducir el audio
        btnPlay.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                updateSeekBar()
            }
        }

        // Pausar el audio
        btnPause.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }

        // Reiniciar el audio
        btnRestart.setOnClickListener {
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
            updateSeekBar()
        }

        // Ir a la actividad de juego
        btnJugar.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
            val intent = Intent(this, ActividadArrastrarYSoltar::class.java)
            startActivity(intent)
        }
    }

    // Método para actualizar la barra de progreso
    private fun updateSeekBar() {
        val audioSeekBar: SeekBar = findViewById(R.id.audioSeekBar)
        val currentTimeText: TextView = findViewById(R.id.currentTimeText)

        // Hacer que la barra de progreso se actualice mientras se reproduce
        val progressRunnable = object : Runnable {
            override fun run() {
                val currentPos = mediaPlayer.currentPosition
                audioSeekBar.progress = currentPos

                val minutes = currentPos / 1000 / 60
                val seconds = currentPos / 1000 % 60
                currentTimeText.text = String.format("%02d:%02d", minutes, seconds)

                // Actualizar cada 1000 ms (1 segundo)
                audioSeekBar.postDelayed(this, 1000)
            }
        }
        progressRunnable.run()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
}
