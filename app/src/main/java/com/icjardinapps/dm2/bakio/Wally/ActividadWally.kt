package com.icjardinapps.dm2.bakio.Wally

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.Kahoot.ActividadBienvenidaKahoot
import com.icjardinapps.dm2.bakio.Mapa.Mapa
import com.icjardinapps.dm2.bakio.R
import com.icjardinapps.dm2.bakio.Sopa.ActividadBienvenidaSopa

/**
 * Actividad principal del juego Wally. El usuario interactúa con tres torres representadas por botones.
 * Una vez que el usuario selecciona las tres torres, puede avanzar a la siguiente actividad.
 */
class ActividadWally : AppCompatActivity() {

    private lateinit var imageButton1: ImageButton
    private lateinit var imageButton2: ImageButton
    private lateinit var imageButton3: ImageButton
    private lateinit var segButton: ImageButton
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView
    private lateinit var imageView: ImageView // Para la imagen de la cara sonriente o triste

    private var selectedCount = 0 // Contador para saber cuántos ImageButtons han sido pulsados
     /**
     * Se ejecuta cuando se crea la actividad. Inicializa las vistas y los listeners para los botones.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wally)

        // Inicializamos las vistas
        imageButton1 = findViewById(R.id.imageButton1)
        imageButton2 = findViewById(R.id.imageButton2)
        imageButton3 = findViewById(R.id.imageButton3)
        segButton = findViewById(R.id.siguiente)
        imageView1 = findViewById(R.id.imageView5)
        imageView2 = findViewById(R.id.imageView8)
        imageView3 = findViewById(R.id.imageView9)

        // Inicialmente ocultamos las imágenes de la torre
        hideTowerImages()

        // Establecemos los listeners para los ImageButtons
        imageButton1.setOnClickListener { onTowerImageClicked(1) }
        imageButton2.setOnClickListener { onTowerImageClicked(2) }
        imageButton3.setOnClickListener { onTowerImageClicked(3) }

        // Instanciar la imagen para la sonrisa/tristeza
        imageView = findViewById(R.id.image)

        // Encuentra el ImageButton de "Volver"
        val buttonBack = findViewById<ImageButton>(R.id.volver)

        buttonBack.setOnClickListener {
            // Crear un Intent para iniciar la actividad de bienvenida
            val intent = Intent(this, ActividadBienvenidaKahoot::class.java)
            startActivity(intent)
            finish() // Opcional, si quieres cerrar la actividad actual
        }

        segButton.setOnClickListener {
            val intent = Intent(this, ActividadBienvenidaKahoot::class.java)
            intent.putExtra("ACTUALIZAR_PUNTOS", true)  // O false, según lo que necesites
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()

        }
    }
    /**
     * Este método se ejecuta cuando el usuario hace clic en uno de los botones de la torre.
     * Desactiva el botón, lo oculta y muestra la imagen correspondiente de la torre seleccionada.
     *
     * @param towerNumber El número de la torre seleccionada (1, 2 o 3).
     */
    private fun onTowerImageClicked(towerNumber: Int) {
        // Deshabilitar y ocultar el ImageButton
        when (towerNumber) {
            1 -> {
                imageButton1.isEnabled = false
                imageButton1.visibility = View.GONE
                showTowerImage(towerNumber)
            }
            2 -> {
                imageButton2.isEnabled = false
                imageButton2.visibility = View.GONE
                showTowerImage(towerNumber)
            }
            3 -> {
                imageButton3.isEnabled = false
                imageButton3.visibility = View.GONE
                showTowerImage(towerNumber)
            }
        }

        // Incrementamos el contador
        selectedCount++

        // Si se han seleccionado las tres torres, habilitamos el botón de Segi
        if (selectedCount == 3) {
            segButton.visibility = View.VISIBLE
        }
    }
    /**
     * Muestra la imagen correspondiente a la torre seleccionada en la interfaz de usuario.
     * También muestra una animación de una cara alegre en la pantalla.
     *
     * @param towerNumber El número de la torre seleccionada (1, 2 o 3).
     */
    private fun showTowerImage(towerNumber: Int) {
        mostrarCaraAlegre()
        // Lógica para mostrar las imágenes de la torre
        val towerImage = when (towerNumber) {
            1 -> R.drawable.torre1
            2 -> R.drawable.torre2
            3 -> R.drawable.torre3
            else -> R.drawable.torre1 // Valor predeterminado
        }

        // Mostrar la imagen correspondiente en el LinearLayout
        when (towerNumber) {
            1 -> imageView1.setImageResource(towerImage)
            2 -> imageView2.setImageResource(towerImage)
            3 -> imageView3.setImageResource(towerImage)
        }

        // Hacer visible la imagen de la torre
        when (towerNumber) {
            1 -> imageView1.visibility = View.VISIBLE
            2 -> imageView2.visibility = View.VISIBLE
            3 -> imageView3.visibility = View.VISIBLE
        }
    }
     /**
     * Oculta todas las imágenes de las torres en la interfaz de usuario.
     */
    private fun hideTowerImages() {
        // Inicialmente, ocultamos las imágenes de la torre en los ImageViews
        imageView1.visibility = View.GONE
        imageView2.visibility = View.GONE
        imageView3.visibility = View.GONE
    }

    /**
     * Muestra una animación de una cara alegre (sonriente) en la pantalla.
     */
    private fun mostrarCaraAlegre() {
        // Cargar animación alegre (sonriente)
        val animationDrawable = resources.getDrawable(R.drawable.sonrisa) as AnimationDrawable
        imageView.setImageDrawable(animationDrawable)
        animationDrawable.start()  // Iniciar la animación
    }

    /**
     * Este metodo se ejecuta cuando el usuario hace clic en el botón "Segi" para avanzar a la siguiente actividad.
     *
     * @param view La vista que fue clickeada.
     */
    fun siguiente(view: View) {
        val intent = Intent(this, ActividadBienvenidaKahoot::class.java)
        startActivity(intent)
        finish() // Opcional, para cerrar la actividad actual si no quieres que el usuario vuelva atrás
    }

}