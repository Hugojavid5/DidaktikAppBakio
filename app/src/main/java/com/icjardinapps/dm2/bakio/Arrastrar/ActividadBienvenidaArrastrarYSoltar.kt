package com.icjardinapps.dm2.bakio.Arrastrar

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R

class ActividadBienvenidaArrastrarYSoltar : AppCompatActivity(), MediaController.MediaPlayerControl {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaController: MediaController
    private lateinit var audioSeekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida_arrastrar)

        // Inicializar el MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.anarruegunaaudioa)

        // Inicializar el MediaController
        mediaController = MediaController(this)
        mediaController.setMediaPlayer(this)
        mediaController.setAnchorView(findViewById(R.id.audioSeekBar))

        val btnRestart: Button = findViewById(R.id.btnRestart)
        val btnJugar: Button = findViewById(R.id.btnJugar)

        // Vincular la barra de progreso y los TextViews de tiempo
        audioSeekBar = findViewById(R.id.audioSeekBar)
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
        updateSeekBar(currentTimeText)

        // Configurar el botón de reinicio
        btnRestart.setOnClickListener {
            mediaPlayer.seekTo(0) // Reiniciar al inicio
            mediaPlayer.start()   // Reproducir desde el principio
            mediaController.show() // Asegurarte de que el MediaController esté visible
        }

        // Ir a la actividad de juego
        btnJugar.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
            val intent = Intent(this, ActividadArrastrarYSoltar::class.java)
            startActivity(intent)
        }

        // Configurar reinicio automático al terminar el audio
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
            mediaController.show()
        }
    }

    // Actualizar la barra de progreso
    private fun updateSeekBar(currentTimeText: TextView) {
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

    // Métodos requeridos para MediaController.MediaPlayerControl
    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun getDuration(): Int {
        return mediaPlayer.duration
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun seekTo(pos: Int) {
        mediaPlayer.seekTo(pos)
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun getBufferPercentage(): Int {
        return (mediaPlayer.currentPosition * 100) / mediaPlayer.duration
    }

    override fun canPause(): Boolean {
        return true
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun canSeekForward(): Boolean {
        return true
    }

    override fun getAudioSessionId(): Int {
        return mediaPlayer.audioSessionId
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
}
