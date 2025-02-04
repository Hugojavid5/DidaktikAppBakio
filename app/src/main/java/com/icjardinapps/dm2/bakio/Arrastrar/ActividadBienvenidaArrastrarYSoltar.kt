package com.icjardinapps.dm2.bakio.Arrastrar

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

class ActividadBienvenidaArrastrarYSoltar : AppCompatActivity(), MediaPlayer.OnCompletionListener {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var audioSeekBar: SeekBar
    private lateinit var btnPlay: ImageButton
    private lateinit var btnPause: ImageButton
    private lateinit var btnRestart: ImageButton
    private lateinit var btnJugar: ImageButton
    private lateinit var currentTimeText: TextView
    private lateinit var totalTimeText: TextView

    private val handler = Handler(Looper.getMainLooper())
    private val progressRunnable = object : Runnable {
        override fun run() {
            try {
                if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                    val currentPos = mediaPlayer.currentPosition
                    audioSeekBar.progress = currentPos

                    val minutes = currentPos / 1000 / 60
                    val seconds = currentPos / 1000 % 60
                    currentTimeText.text = String.format("%02d:%02d", minutes, seconds)

                    handler.postDelayed(this, 1000)
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace() // Evita el crash si el MediaPlayer ya fue liberado
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida_arrastrar)

        mediaPlayer = MediaPlayer.create(this, R.raw.anarruegunaaudioa)

        audioSeekBar = findViewById(R.id.audioSeekBar)
        btnPlay = findViewById(R.id.btn_play)
        btnPause = findViewById(R.id.btn_pause)
        btnRestart = findViewById(R.id.btn_reiniciar)
        btnJugar = findViewById(R.id.btnJugar)
        currentTimeText = findViewById(R.id.currentTimeText)
        totalTimeText = findViewById(R.id.totalTimeText)

        val totalDuration = mediaPlayer.duration
        val totalDurationFormatted = String.format("%02d:%02d", totalDuration / 1000 / 60, totalDuration / 1000 % 60)
        totalTimeText.text = totalDurationFormatted

        audioSeekBar.max = totalDuration
        handler.post(progressRunnable) // Inicia el Runnable para actualizar la barra de progreso

        btnPlay.setOnClickListener {
            mediaPlayer.start()
            btnPlay.isEnabled = false
            btnPause.isEnabled = true
        }

        btnPause.setOnClickListener {
            mediaPlayer.pause()
            btnPlay.isEnabled = true
            btnPause.isEnabled = false
        }

        btnRestart.setOnClickListener {
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
            btnPlay.isEnabled = false
            btnPause.isEnabled = true
        }

        btnJugar.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
            val intent = Intent(this, ActividadArrastrarYSoltar::class.java)
            startActivity(intent)
        }

        mediaPlayer.setOnCompletionListener(this)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mediaPlayer.seekTo(0)
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(progressRunnable) // Detiene el Runnable
        if (::mediaPlayer.isInitialized) {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.release()
        }
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // No hacemos nada, por lo que no se realizará ninguna acción al presionar la flecha de retroceso
    }

}