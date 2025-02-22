package com.icjardinapps.dm2.bakio.Kahoot

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R
import android.media.MediaPlayer
/**
 * Actividad que gestiona la interfaz de usuario para controlar la reproducción de un audio
 * con opciones de reproducción, pausa, reinicio y un botón para empezar un juego tipo Kahoot.
 * También permite al usuario controlar el progreso del audio mediante un SeekBar.
 */
class ActividadBienvenidaKahoot : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var botonJugar: ImageButton // Botón para jugar
    private lateinit var botonPlay: ImageButton // Botón de play
    private lateinit var botonPause: ImageButton // Botón de pause
    private lateinit var botonReiniciar: ImageButton // Botón de reiniciar
    private lateinit var seekBar: SeekBar // Seekbar para el progreso
    private lateinit var txtCurrentTime: TextView // Texto con el tiempo actual
    private lateinit var txtTotalTime: TextView // Texto con el tiempo total
    private val handler = Handler() // Handler para actualizar el SeekBar
/**
     * Método que se ejecuta al crear la actividad.
     * Inicializa los componentes de la interfaz de usuario y configura la lógica del audio y los controles.
     *
     * @param savedInstanceState Estado guardado de la actividad (si existe).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida_kahoot)

        // Inicializa el MediaPlayer con el archivo de audio
        mediaPlayer = MediaPlayer.create(this, R.raw.amaierakoaudioa)

        // Configura los botones
        botonJugar = findViewById(R.id.btnJugar)
        botonPlay = findViewById(R.id.btn_play)
        botonPause = findViewById(R.id.btn_pause)
        botonReiniciar = findViewById(R.id.btn_reiniciar)

        // Configura el SeekBar
        seekBar = findViewById(R.id.seekBar)
        seekBar.max = mediaPlayer.duration

        // Configura los TextViews
        txtCurrentTime = findViewById(R.id.txtCurrentTime)
        txtTotalTime = findViewById(R.id.txtTotalTime)
        txtTotalTime.text = formatTime(mediaPlayer.duration)

        // Configura el botón Jugar para redirigir a otra actividad y detener el audio
        botonJugar.setOnClickListener {
            mediaPlayer.pause() // Detenemos el audio cuando se pulsa el botón de jugar
            val intent = Intent(this, ActividadKahoot::class.java)
            startActivity(intent)
            finish() // Finaliza la actividad actual para evitar que se quede en el stack de actividades
        }

        // Configura el botón Play
        botonPlay.setOnClickListener {
            mediaPlayer.start() // Comienza la reproducción del audio
            botonPlay.isEnabled = false
            botonPause.isEnabled = true
            updateSeekBar()
        }

        // Configura el botón Pause
        botonPause.setOnClickListener {
            mediaPlayer.pause() // Pausa la reproducción del audio
            botonPlay.isEnabled = true
            botonPause.isEnabled = false
        }

        // Configura el botón Reiniciar
        botonReiniciar.setOnClickListener {
            mediaPlayer.seekTo(0) // Reinicia el audio
            mediaPlayer.start() // Reproduce el audio desde el principio
            botonPlay.isEnabled = false
            botonPause.isEnabled = true
            updateSeekBar()
        }

        // Actualiza el SeekBar mientras se reproduce el audio
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
     * Actualiza el SeekBar y el tiempo actual de reproducción cada segundo.
     */
    private fun updateSeekBar() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                seekBar.progress = mediaPlayer.currentPosition
                txtCurrentTime.text = formatTime(mediaPlayer.currentPosition)
                if (mediaPlayer.isPlaying) {
                    handler.postDelayed(this, 1000)
                }
            }
        }, 1000)
    }
/**
     * Formatea el tiempo en milisegundos a un formato de minutos y segundos (MM:SS).
     *
     * @param milliseconds El tiempo en milisegundos.
     * @return El tiempo formateado como "MM:SS".
     */
    private fun formatTime(milliseconds: Int): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / 1000) / 60
        return String.format("%02d:%02d", minutes, seconds)
    }
     /**
     * Libera los recursos del MediaPlayer cuando se destruye la actividad.
     */
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release() // Libera recursos al destruir la actividad
    }
    /**
     * Sobrescribe la acción de la flecha de retroceso para evitar que se haga algo al presionar el botón de retroceso.
     */
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // No hacemos nada, por lo que no se realizará ninguna acción al presionar la flecha de retroceso
    }
}
