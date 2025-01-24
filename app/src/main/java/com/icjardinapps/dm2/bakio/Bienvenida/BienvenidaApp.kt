package com.icjardinapps.dm2.bakio.Bienvenida

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.Mapa.Mapa
import com.icjardinapps.dm2.bakio.R



class BienvenidaApp : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida_app)

        // Inicializar el reproductor de audio
        mediaPlayer = MediaPlayer.create(this, R.raw.hasierakopantailaaudio)

        // Vincular los botones desde el XML
        val btn_play: Button = findViewById(R.id.btn_play)
        val btn_pause: Button = findViewById(R.id.btn_pause)
        val btn_reiniciar: Button = findViewById(R.id.btn_reiniciar)
        val ir_mapa: Button = findViewById(R.id.ir_mapa)

        // Vincular los TextViews de tiempo
        val currentTimeText: TextView = findViewById(R.id.currentTimeText)
        val totalTimeText: TextView = findViewById(R.id.totalTimeText)

        // Vincular la barra de progreso (SeekBar)
        val audioSeekBar: SeekBar = findViewById(R.id.audioSeekBar)

        // Configurar el comportamiento de los botones
        btn_play.setOnClickListener {
            mediaPlayer?.start() // Reproducir el audio
        }

        btn_pause.setOnClickListener {
            mediaPlayer?.pause() // Pausar el audio
        }

        btn_reiniciar.setOnClickListener {
            mediaPlayer?.seekTo(0) // Reiniciar el audio desde el inicio
            mediaPlayer?.start()   // Reproducir el audio después de reiniciar
        }


        ir_mapa.setOnClickListener {
            mediaPlayer?.pause() // Pausar el audio antes de cambiar de actividad
            // Navegar a la actividad del mapa
            val intent = Intent(this, Mapa::class.java)
            startActivity(intent)
        }

        // Obtener la duración total del audio
        val totalDuration = mediaPlayer?.duration ?: 0
        val minutes = totalDuration / 1000 / 60
        val seconds = totalDuration / 1000 % 60
        val totalDurationFormatted = String.format("%02d:%02d", minutes, seconds)

        // Establecer la duración total en el TextView
        totalTimeText.text = totalDurationFormatted

        // Configurar el SeekBar
        audioSeekBar.max = totalDuration

        // Actualizar el progreso y el tiempo transcurrido en el audio
        mediaPlayer?.setOnPreparedListener {
            val progressRunnable = object : Runnable {
                override fun run() {
                    val currentPos = mediaPlayer?.currentPosition ?: 0
                    audioSeekBar.progress = currentPos

                    val minutes = currentPos / 1000 / 60
                    val seconds = currentPos / 1000 % 60
                    currentTimeText.text = String.format("%02d:%02d", minutes, seconds)

                    // Actualizar cada segundo
                    audioSeekBar.postDelayed(this, 1000)
                }
            }
            progressRunnable.run()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Liberar recursos del MediaPlayer para evitar fugas de memoria
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
