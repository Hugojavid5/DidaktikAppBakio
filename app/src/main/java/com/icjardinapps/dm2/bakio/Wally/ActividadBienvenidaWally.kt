package com.icjardinapps.dm2.bakio.Wally
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R


class ActividadBienvenidaWally : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button
    private lateinit var restartButton: Button
    private lateinit var jugarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida_wally)

        // Inicializar el MediaPlayer con el audio
        mediaPlayer = MediaPlayer.create(this, R.raw.wally)  // Cambia el archivo de audio
        seekBar = findViewById(R.id.audioSeekBar)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)
        restartButton = findViewById(R.id.restartButton)
        jugarButton = findViewById(R.id.jugarButton)

        // Configuración inicial de la barra de progreso
        seekBar.max = mediaPlayer.duration
        seekBar.progress = 0

        // Actualizar la barra de progreso mientras se reproduce el audio
        mediaPlayer.setOnPreparedListener {
            val runnable = Runnable {
                while (mediaPlayer.isPlaying) {
                    seekBar.progress = mediaPlayer.currentPosition
                    Thread.sleep(1000)
                }
            }
            Thread(runnable).start()
        }

        // Control de botones
        playButton.setOnClickListener {
            mediaPlayer.start()
        }

        pauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }

        restartButton.setOnClickListener {
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
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
}
