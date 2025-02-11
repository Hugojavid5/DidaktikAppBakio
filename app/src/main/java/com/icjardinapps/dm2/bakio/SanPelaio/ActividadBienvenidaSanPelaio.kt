package com.icjardinapps.dm2.bakio.SanPelaio
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
 * Actividad que gestiona la bienvenida a la sección de San Pelaio y reproduce un archivo de audio.
 * Esta actividad permite al usuario reproducir, pausar, reiniciar y navegar a una nueva actividad del juego.
 */
class ActividadBienvenidaSanPelaio : AppCompatActivity(){
    private lateinit var mediaPlayer: MediaPlayer
/**
     * Método que se llama cuando la actividad se crea.
     * Inicializa los controles de la interfaz de usuario y configura el reproductor de audio.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida_san_pelaio)

        val btnPlay = findViewById<ImageButton>(R.id.btn_play)
        val btnPause = findViewById<ImageButton>(R.id.btn_pause)
        val btnRestart = findViewById<ImageButton>(R.id.btn_reiniciar)
        val btnPlayGame = findViewById<ImageButton>(R.id.btnJugar)

        val audioSeekBar = findViewById<SeekBar>(R.id.audioSeekBar)
        val currentTimeText = findViewById<TextView>(R.id.currentTimeText)
        val totalTimeText = findViewById<TextView>(R.id.totalTimeText)

        // Initialize MediaPlayer with audio file
        mediaPlayer = MediaPlayer.create(this, R.raw.sanpelaioaudioa)

        // Set the total duration of the audio
        val totalDuration = mediaPlayer.duration
        audioSeekBar.max = totalDuration
        totalTimeText.text = formatTime(totalDuration)

        btnPlay.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                updateSeekBar()
            }
        }

        btnPause.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }

        btnRestart.setOnClickListener {
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
            updateSeekBar()
        }

        btnPlayGame.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
            // Navigate to the game activity
            val intent = Intent(this, ActividadSanPelaio::class.java)
            startActivity(intent)
        }

        // Update SeekBar position while the audio is playing
        audioSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
     * Actualiza el SeekBar y la visualización del tiempo actual del audio mientras se reproduce.
     */
    private fun updateSeekBar() {
        val updateSeekBarRunnable = object : Runnable {
            override fun run() {
                val currentPosition = mediaPlayer.currentPosition
                findViewById<SeekBar>(R.id.audioSeekBar).progress = currentPosition
                findViewById<TextView>(R.id.currentTimeText).text = formatTime(currentPosition)

                if (mediaPlayer.isPlaying) {
                    findViewById<SeekBar>(R.id.audioSeekBar).postDelayed(this, 1000)
                }
            }
        }
        findViewById<SeekBar>(R.id.audioSeekBar).post(updateSeekBarRunnable)
    }
 /**
     * Formatea el tiempo en milisegundos en formato [MM:SS].
     *
     * @param timeInMillis Tiempo en milisegundos a formatear
     * @return Cadena que representa el tiempo en formato [MM:SS]
     */
    private fun formatTime(timeInMillis: Int): String {
        val minutes = timeInMillis / 1000 / 60
        val seconds = timeInMillis / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
/**
     * Se llama cuando la actividad es destruida.
     * Libera los recursos del MediaPlayer si está en reproducción.
     */
    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
    /**
     * Metodo que se llama al presionar el botón de retroceso.
     * Se sobrescribe para evitar que la actividad se cierre al presionar la flecha de retroceso.
     */
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // No hacemos nada, por lo que no se realizará ninguna acción al presionar la flecha de retroceso
    }
}
