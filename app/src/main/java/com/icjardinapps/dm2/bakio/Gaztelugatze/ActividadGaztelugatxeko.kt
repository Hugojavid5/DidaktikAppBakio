package com.icjardinapps.dm2.bakio.Gaztelugatze

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.Mapa.Mapa
import com.icjardinapps.dm2.bakio.R
/**
 * Actividad que maneja la interacción con el usuario en la sección de Gaztelugatxe.
 * Reproduce un video, presenta una pregunta con opciones de respuesta,
 * y permite al usuario corregir su respuesta y avanzar al siguiente paso.
 */
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
/**
     * Método que se ejecuta al crear la actividad.
     * Inicializa los componentes de la interfaz de usuario y configura la lógica del video y las respuestas.
     *
     * @param savedInstanceState Estado guardado de la actividad (si existe).
     */
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
                resultado.text = getString(R.string.respuesta_correcta)
                mostrarCaraAlegre()

                // Habilitar el botón de siguiente si la respuesta es correcta
                siguienteButton.isEnabled = true
                siguienteButton.alpha = 1.0f // Restaurar la opacidad para indicar que está activado
            } else {
                resultado.text = getString(R.string.respuesta_incorrecta)
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
            val intent = Intent(this, Mapa::class.java)
            intent.putExtra("ACTUALIZAR_PUNTOS", true)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()

        }

        // Iniciar el video automáticamente
        videoView.start()
    }
 /**
     * Configura el VideoView para que ocupe toda la pantalla.
     */
    private fun hacerPantallaCompleta() {
        // Configurar VideoView para que ocupe toda la pantalla
        val layoutParams = videoView.layoutParams
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
        videoView.layoutParams = layoutParams
    }
 /**
     * Muestra una animación de una cara triste cuando la respuesta es incorrecta.
     */
    private fun mostrarCaraTriste() {
        val animationDrawable = resources.getDrawable(R.drawable.triste) as AnimationDrawable
        imageView.setImageDrawable(animationDrawable)
        animationDrawable.start()
    }
/**
     * Muestra una animación de una cara alegre cuando la respuesta es correcta.
     */
    private fun mostrarCaraAlegre() {
        val animationDrawable = resources.getDrawable(R.drawable.sonrisa) as AnimationDrawable
        imageView.setImageDrawable(animationDrawable)
        animationDrawable.start()
    }
}
