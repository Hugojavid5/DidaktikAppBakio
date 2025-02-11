package com.icjardinapps.dm2.bakio.Gaztelugatze

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R

/**
 * Actividad de bienvenida para la sección de Gaztelugatxe.
 * Incluye una imagen, una explicación y un reproductor de audio con controles.
 */
class ActividadBienvenidaGaztelugatxeko : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var audioSeekBar: SeekBar
    private lateinit var currentTimeText: TextView
    private lateinit var totalTimeText: TextView
    private val handler = Handler()
/**
     * Método que se ejecuta al crear la actividad.
     * Inicializa la interfaz de usuario, configura el reproductor de audio
     * y gestiona los botones de reproducción.
     *
     * @param savedInstanceState Estado guardado de la actividad (si existe).
     */
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
        val playButton: ImageButton = findViewById(R.id.btn_play)
        playButton.setOnClickListener {
            if (mediaPlayer?.isPlaying == false) {
                mediaPlayer?.start()
            }
        }

        // Acción del botón Pause
        val pauseButton: ImageButton = findViewById(R.id.btn_pause)
        pauseButton.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }

        // Acción del botón Reiniciar
        val restartButton: ImageButton = findViewById(R.id.btn_reiniciar)
        restartButton.setOnClickListener {
            try {
                // Detener y liberar el MediaPlayer si está en uso
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                }

                // Crear un nuevo MediaPlayer y configurar el audio
                mediaPlayer = MediaPlayer.create(this, R.raw.gaztelugatxeaudioa)

                // Volver a configurar la duración total del audio y el SeekBar
                val totalDuration = mediaPlayer?.duration ?: 0
                totalTimeText.text = formatTime(totalDuration)
                audioSeekBar.max = totalDuration
                audioSeekBar.progress = 0

                // Iniciar el audio desde el inicio
                mediaPlayer?.start()

            } catch (e: Exception) {
                e.printStackTrace()  // Imprimir error si algo sale mal
            }
        }


        // Acción del botón Jugar
        val playGameButton: ImageButton = findViewById(R.id.btnJugar)
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

    /**
     * Formatea un tiempo en milisegundos a formato mm:ss.
     *
     * @param milliseconds Tiempo en milisegundos.
     * @return Cadena en formato "mm:ss".
     */
    private fun formatTime(milliseconds: Int): String {
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
/**
     * Se ejecuta cuando la actividad es destruida.
     * Libera los recursos del MediaPlayer para evitar fugas de memoria.
     */
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
    /**
     * Sobrescribe el comportamiento del botón "Atrás" para evitar que el usuario retroceda.
     */
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // No hacemos nada, por lo que no se realizará ninguna acción al presionar la flecha de retroceso
    }
}
