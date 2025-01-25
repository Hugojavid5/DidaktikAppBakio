package com.icjardinapps.dm2.bakio.Arrastrar

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R

class ActividadBienvenidaArrastrarYSoltar : AppCompatActivity(), MediaPlayer.OnCompletionListener {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var audioSeekBar: SeekBar
    private lateinit var btnPlay: ImageButton
    private lateinit var btnPause: ImageButton
    private lateinit var btnRestart: ImageButton
    private lateinit var btnJugar: ImageButton
    private lateinit var currentTimeText: TextView
    private lateinit var totalTimeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida_arrastrar)

        // Inicializar los elementos de la UI
        mediaPlayer = MediaPlayer.create(this, R.raw.anarruegunaaudioa)

        audioSeekBar = findViewById(R.id.audioSeekBar)
        btnPlay = findViewById(R.id.btn_play)
        btnPause = findViewById(R.id.btn_pause)
        btnRestart = findViewById(R.id.btn_reiniciar)
        btnJugar = findViewById(R.id.btnJugar)
        currentTimeText = findViewById(R.id.currentTimeText)
        totalTimeText = findViewById(R.id.totalTimeText)

        // Obtener la duración total del audio
        val totalDuration = mediaPlayer.duration
        val minutes = totalDuration / 1000 / 60
        val seconds = totalDuration / 1000 % 60
        val totalDurationFormatted = String.format("%02d:%02d", minutes, seconds)
        totalTimeText.text = totalDurationFormatted

        // Configurar la SeekBar
        audioSeekBar.max = totalDuration
        updateSeekBar()

        // Botón Play
        btnPlay.setOnClickListener {
            mediaPlayer.start()
            btnPlay.isEnabled = false
            btnPause.isEnabled = true
        }

        // Botón Pause
        btnPause.setOnClickListener {
            mediaPlayer.pause()
            btnPlay.isEnabled = true
            btnPause.isEnabled = false
        }

        // Botón Reiniciar
        btnRestart.setOnClickListener {
            mediaPlayer.seekTo(0) // Reiniciar al inicio
            mediaPlayer.start()   // Reproducir desde el principio
            btnPlay.isEnabled = false
            btnPause.isEnabled = true
        }

        // Botón Jugar
        btnJugar.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
            val intent = Intent(this, ActividadArrastrarYSoltar::class.java)
            startActivity(intent)
        }

        // Configurar evento cuando termina el audio
        mediaPlayer.setOnCompletionListener(this)
    }

    private fun updateSeekBar() {
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

    // Metodo para reiniciar el audio cuando termina
    override fun onCompletion(mp: MediaPlayer?) {
        mediaPlayer.seekTo(0)
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
}
