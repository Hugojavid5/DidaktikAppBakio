package com.icjardinapps.dm2.bakio.Gaztelugatze

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.Mapa.Mapa
import com.icjardinapps.dm2.bakio.R

class ActividadGaztelugatxeko : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var pregunta: TextView
    private lateinit var respuestas: RadioGroup
    private lateinit var corregirBtn: ImageButton
    private lateinit var resultado: TextView
    private lateinit var imageView: ImageView
    private lateinit var volverButton: ImageButton
    private lateinit var siguienteButton: ImageButton
    private val respuestaCorrecta = R.id.respuesta2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gaztelugatxeko)

        // Vincular vistas
        videoView = findViewById(R.id.videoView)
        pregunta = findViewById(R.id.pregunta)
        respuestas = findViewById(R.id.respuestas)
        corregirBtn = findViewById(R.id.corregirBtn)
        resultado = findViewById(R.id.resultados)
        imageView = findViewById(R.id.image)
        volverButton = findViewById(R.id.btn_volver)
        siguienteButton = findViewById(R.id.siguienteButton)

        // Configurar el video
        val videoPath = "android.resource://" + packageName + "/" + R.raw.video
        videoView.setVideoPath(videoPath)
        val mediaController = MediaController(this)
        videoView.setMediaController(mediaController)

        // Hacer el VideoView pantalla completa
        hacerPantallaCompleta()

        // Inicialmente deshabilitar los botones de corrección y siguiente
        corregirBtn.isEnabled = false
        corregirBtn.visibility = View.INVISIBLE
        siguienteButton.isEnabled = false
        siguienteButton.alpha = 0.5f // Cambiar la opacidad para indicar que está desactivado

        // Mostrar pregunta y respuestas cuando el video comience
        videoView.setOnPreparedListener {
            pregunta.visibility = View.VISIBLE
            respuestas.visibility = View.VISIBLE
        }

        // Habilitar el botón de corrección después de que el video termine
        videoView.setOnCompletionListener {
            corregirBtn.isEnabled = true
            corregirBtn.visibility = View.VISIBLE
        }

        // Manejar clic en el botón de corrección
        corregirBtn.setOnClickListener {
            val selectedId = respuestas.checkedRadioButtonId
            if (selectedId == respuestaCorrecta) {
                resultado.text = "¡Respuesta Correcta!"
                mostrarCaraAlegre()

                // Habilitar el botón de siguiente si la respuesta es correcta
                siguienteButton.isEnabled = true
                siguienteButton.alpha = 1.0f // Restaurar la opacidad para indicar que está activado
            } else {
                resultado.text = "Respuesta Incorrecta."
                mostrarCaraTriste()
            }
            resultado.visibility = View.VISIBLE
            imageView.visibility = View.VISIBLE
        }

        // Acción del botón Volver
        volverButton.setOnClickListener {
            // Regresar a la pantalla de bienvenida (MainActivity)
            val intent = Intent(this, ActividadBienvenidaGaztelugatxeko::class.java)
            startActivity(intent)
            finish()
        }

        // Acción del botón Siguiente
        siguienteButton.setOnClickListener {
            // Redirigir a la clase MapaActivity
            val intent = Intent(this, Mapa::class.java)
            startActivity(intent)
            finish()
        }

        // Iniciar el video automáticamente
        videoView.start()
    }

    private fun hacerPantallaCompleta() {
        // Configurar VideoView para que ocupe toda la pantalla
        val layoutParams = videoView.layoutParams
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
        videoView.layoutParams = layoutParams
    }

    private fun mostrarCaraTriste() {
        val animationDrawable = resources.getDrawable(R.drawable.triste) as AnimationDrawable
        imageView.setImageDrawable(animationDrawable)
        animationDrawable.start()
    }

    private fun mostrarCaraAlegre() {
        val animationDrawable = resources.getDrawable(R.drawable.sonrisa) as AnimationDrawable
        imageView.setImageDrawable(animationDrawable)
        animationDrawable.start()
    }
}
