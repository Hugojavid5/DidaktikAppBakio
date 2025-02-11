package com.icjardinapps.dm2.bakio.Arrastrar

import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.Bela.ActividadBienvenidaBela
import com.icjardinapps.dm2.bakio.Mapa.Mapa
import com.icjardinapps.dm2.bakio.R
/**
 * Clase que gestiona una actividad de arrastrar y soltar, donde los usuarios deben
 * emparejar elementos con sus respectivas siluetas. Proporciona retroalimentación
 * visual y maneja respuestas correctas e incorrectas.
 */
class ActividadArrastrarYSoltar : AppCompatActivity() {

     // Elementos de la interfaz gráfica (siluetas e imágenes arrastrables)
    private lateinit var siluetaHojas: ImageView
    private lateinit var siluetaPaja: ImageView
    private lateinit var siluetaSaco: ImageView
    private lateinit var siluetaPoncho: ImageView
    private lateinit var hojas: ImageView
    private lateinit var paja: ImageView
    private lateinit var saco: ImageView
    private lateinit var poncho: ImageView
    private lateinit var btnSalir: ImageButton
    private lateinit var imageView: ImageView // Para la imagen de la cara sonriente o triste

    private var correctMatches = 0 // Contador de emparejamientos correctos

    /**
     * Método que se ejecuta al crear la actividad.
     * @param savedInstanceState Estado guardado de la instancia anterior (si existe).
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_arrastrar)

        // Instanciar los elementos del layout
        siluetaHojas = findViewById(R.id.silueta_hojas)
        siluetaPaja = findViewById(R.id.silueta_paja)
        siluetaSaco = findViewById(R.id.silueta_saco)
        siluetaPoncho = findViewById(R.id.silueta_poncho)

        hojas = findViewById(R.id.hojas)
        paja = findViewById(R.id.paja)
        saco = findViewById(R.id.saco)
        poncho = findViewById(R.id.poncho)

        btnSalir = findViewById(R.id.imageButtonNextActivity)
        btnSalir.visibility = View.INVISIBLE // Esconder botón al inicio

        // Instanciar la imagen para la sonrisa/tristeza
        imageView = findViewById(R.id.image)

        // Configurar los elementos para arrastrar
        setDraggable(hojas, "hojas")
        setDraggable(paja, "paja")
        setDraggable(saco, "saco")
        setDraggable(poncho, "poncho")

        // Configurar las siluetas para soltar
        setDroppable(siluetaHojas, "hojas", R.drawable.hojas)
        setDroppable(siluetaPaja, "paja", R.drawable.paja)
        setDroppable(siluetaSaco, "saco", R.drawable.saco)
        setDroppable(siluetaPoncho, "poncho", R.drawable.poncho)

        btnSalir.setOnClickListener {
            try {
                val intent = Intent(this, Mapa::class.java)
                intent.putExtra("ACTUALIZAR_PUNTOS", true)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error al cambiar de pantalla", Toast.LENGTH_LONG).show()
            }
        }


    }

    /**
     * Muestra la imagen de una cara triste cuando se produce un error en el emparejamiento.
     */
    private fun mostrarCaraTriste() {
        val animationDrawable = resources.getDrawable(R.drawable.triste) as AnimationDrawable
        imageView.setImageDrawable(animationDrawable)
        animationDrawable.start()  // Asegúrate de tener la imagen "triste" en res/drawable
    }

    /**
     * Muestra la imagen de una cara sonriente cuando se realiza un emparejamiento correcto.
     */
    private fun mostrarCaraAlegre() {
        // Cargar animación alegre (sonriente)
        val animationDrawable = resources.getDrawable(R.drawable.sonrisa) as AnimationDrawable
        imageView.setImageDrawable(animationDrawable)
        animationDrawable.start()  // Iniciar la animación
    }

   /**
     * Configura un elemento como "arrastrable" para que pueda ser movido a su silueta correspondiente.
     * @param imageView Imagen que se hará arrastrable.
     * @param tag Etiqueta que identifica el tipo de elemento.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setDraggable(imageView: ImageView, tag: String) {
        imageView.tag = tag // Asignar un tag único
        imageView.setOnTouchListener { v, _ ->
            val item = ClipData.Item(v.tag as CharSequence)
            val dragData = ClipData(
                v.tag as CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            val shadow = View.DragShadowBuilder(v)
            v.startDragAndDrop(dragData, shadow, v, 0)
            true
        }
    }

  /**
     * Configura una silueta como "receptora" de un elemento arrastrado.
     * @param imageView Imagen de la silueta.
     * @param expectedTag Etiqueta esperada para considerar el emparejamiento como correcto.
     * @param correctImageRes Imagen que se mostrará al realizar el emparejamiento correcto.
     */
    private fun setDroppable(imageView: ImageView, expectedTag: String, correctImageRes: Int) {
        imageView.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    event.clipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) ?: false
                }

                DragEvent.ACTION_DRAG_ENTERED -> {
                    v.alpha = 0.7f // Efecto visual al entrar
                    true
                }

                DragEvent.ACTION_DRAG_EXITED -> {
                    v.alpha = 1.0f // Quitar efecto visual
                    true
                }

                DragEvent.ACTION_DROP -> {
                    val item = event.clipData.getItemAt(0)
                    val dragData = item.text.toString()

                    if (dragData == expectedTag) {
                        // Emparejamiento correcto
                        mostrarCaraAlegre()
                        val draggedView = event.localState as ImageView
                        draggedView.visibility = View.INVISIBLE // Ocultar imagen arrastrada
                        (v as ImageView).apply {
                            alpha = 1.0f // Restaurar opacidad
                            setImageResource(correctImageRes) // Cambiar la silueta por la imagen correcta
                        }
                        correctMatches++

                        if (correctMatches == 4) {
                            btnSalir.visibility = View.VISIBLE // Mostrar botón salir
                            Toast.makeText(this,
                                getString(R.string.juego_completado), Toast.LENGTH_SHORT).show()
                            mostrarCaraAlegre() // Mostrar cara alegre al completar el juego
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.intenta_nuevo), Toast.LENGTH_SHORT).show()
                        mostrarCaraTriste() // Mostrar cara triste si hay un fallo
                    }
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    v.alpha = 1.0f // Restaurar opacidad en cualquier caso
                    true
                }

                else -> false
            }
        }
    }
}
