package com.icjardinapps.dm2.bakio.Wally

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R


class ActividadWally : AppCompatActivity() {

    private lateinit var imageButton1: ImageButton
    private lateinit var imageButton2: ImageButton
    private lateinit var imageButton3: ImageButton
    private lateinit var segButton: Button
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView

    private var selectedCount = 0 // Contador para saber cuántos ImageButtons han sido pulsados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wally)

        // Inicializamos las vistas
        imageButton1 = findViewById(R.id.imageButton1)
        imageButton2 = findViewById(R.id.imageButton2)
        imageButton3 = findViewById(R.id.imageButton3)
        segButton = findViewById(R.id.button)
        imageView1 = findViewById(R.id.imageView5)
        imageView2 = findViewById(R.id.imageView8)
        imageView3 = findViewById(R.id.imageView9)

        // Inicialmente ocultamos las imágenes de la torre
        hideTowerImages()

        // Establecemos los listeners para los ImageButtons
        imageButton1.setOnClickListener { onTowerImageClicked(1) }
        imageButton2.setOnClickListener { onTowerImageClicked(2) }
        imageButton3.setOnClickListener { onTowerImageClicked(3) }
    }

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

    private fun showTowerImage(towerNumber: Int) {
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

    private fun hideTowerImages() {
        // Inicialmente, ocultamos las imágenes de la torre en los ImageViews
        imageView1.visibility = View.GONE
        imageView2.visibility = View.GONE
        imageView3.visibility = View.GONE
    }

    // Este metodo se ejecuta al hacer clic en el botón "Segi"
    fun siguiente(view: View) {
        // Aquí puedes poner la lógica que debe ocurrir cuando el botón Segi sea pulsado
        Toast.makeText(this, "Actividad completada", Toast.LENGTH_SHORT).show()
    }
}
