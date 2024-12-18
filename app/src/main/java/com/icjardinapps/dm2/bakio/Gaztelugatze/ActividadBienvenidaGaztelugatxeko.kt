package com.icjardinapps.dm2.bakio.Gaztelugatze

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R


class ActividadBienvenidaGaztelugatxeko : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var audioSeekBar: SeekBar
    private lateinit var currentTimeText: TextView
    private lateinit var totalTimeText: TextView
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida_gaztelugatxeko)

        // Configurar MediaPlayer con un archivo de audio en res/raw
        mediaPlayer = MediaPlayer.create(this, R.raw.gaztelugatxeaudioa)

        // Configurar ImageView para la imagen de bienvenida
        val bienvenidaImageView: ImageView = findViewById(R.id.iv_bienvenida)

        // Configurar TextView para la explicación
        val explicacionTextView: TextView = findViewById(R.id.tv_explicacion)

        // Configurar SeekBar para la barra de audio
        audioSeekBar = findViewById(R.id.audioSeekBar)
        currentTimeText = findViewById(R.id.currentTimeText)
        totalTimeText = findViewById(R.id.totalTimeText)

        // Configurar la duración total del audio
        val totalDuration = mediaPlayer?.duration ?: 0
        totalTimeText.text = formatTime(totalDuration)

        // Configurar SeekBar
        audioSeekBar.max = totalDuration
        audioSeekBar.progress = 0

        // Actualizar el tiempo en la barra y en los TextViews
        mediaPlayer?.setOnPreparedListener {
            // Iniciar un hilo para actualizar el progreso del SeekBar mientras se reproduce el audio
            handler.postDelayed(object : Runnable {
                override fun run() {
                    val currentPosition = mediaPlayer?.currentPosition ?: 0
                    audioSeekBar.progress = currentPosition
                    currentTimeText.text = formatTime(currentPosition)
                    handler.postDelayed(this, 1000) // Actualiza cada segundo
                }
            }, 0)
        }

        // Acción del botón Play
        val playButton: Button = findViewById(R.id.btn_play)
        playButton.setOnClickListener {
            if (mediaPlayer?.isPlaying == false) {
                mediaPlayer?.start()
            }
        }

        // Acción del botón Pause
        val pauseButton: Button = findViewById(R.id.btn_pause)
        pauseButton.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }

        // Acción del botón Reiniciar
        val restartButton: Button = findViewById(R.id.btn_restart)
        restartButton.setOnClickListener {
            try {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.stop()
                }
                mediaPlayer?.prepare()  // Necesario para reiniciar el MediaPlayer
                mediaPlayer?.start()    // Comienza a reproducir el audio desde el inicio
            } catch (e: Exception) {
                e.printStackTrace()  // Imprimir error si algo sale mal
            }
        }

        // Acción del botón Jugar
        val playGameButton: Button = findViewById(R.id.btn_play_game)
        playGameButton.setOnClickListener {
            // Detener el audio si está reproduciéndose
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
                mediaPlayer?.prepare()  // Necesario para reiniciar el MediaPlayer
            }

            // Abre la actividad ActividadGaztelugatxeko
            val intent = Intent(this, ActividadGaztelugatxeko::class.java)
            startActivity(intent)
        }
    }

    // Función para formatear el tiempo en minutos:segundos
    private fun formatTime(milliseconds: Int): String {
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
