package com.icjardinapps.dm2.bakio.Bela

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.Mapa.Mapa
import com.icjardinapps.dm2.bakio.R

class ActividadBela : AppCompatActivity() {

        private lateinit var pieza1: ImageView
        private lateinit var pieza2: ImageView
        private lateinit var triangulo: ImageView
        private lateinit var pieza4: ImageView
        private lateinit var pieza5: ImageView
        private lateinit var pegote: ImageView
        private lateinit var velaOscura: ImageView
        private lateinit var btnSalir: ImageButton

        private var correctMatches = 0 // Contador de emparejamientos correctos

        @SuppressLint("MissingInflatedId")
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            supportActionBar?.hide()
            setContentView(R.layout.activity_vela)

            // Instanciar los elementos del layout
            pieza1 = findViewById(R.id.pieza1)
            pieza2 = findViewById(R.id.pieza2)
            triangulo = findViewById(R.id.triangulo)
            pieza4 = findViewById(R.id.pieza4)
            pieza5 = findViewById(R.id.pieza5)
            pegote = findViewById(R.id.pegote)
            velaOscura = findViewById(R.id.velaOscura)

            btnSalir = findViewById(R.id.btnSalir)
            btnSalir.visibility = View.INVISIBLE // Esconder botón al inicio


            setDraggable(pieza1, "pieza1")
            setDraggable(pieza2, "pieza2")
            setDraggable(triangulo, "triangulo")
            setDraggable(pieza4, "pieza4")
            setDraggable(pieza5, "pieza5")
            setDraggable(pegote, "pegote")

            // Configurar las siluetas para soltar
            setDroppable(velaOscura, "velaoscura", R.drawable.hojas)


            // Configurar el botón salir
            btnSalir.setOnClickListener {
                // Regresar al mapa o cerrar la actividad
                val intent = Intent(this, Mapa::class.java)
                startActivity(intent)
                finish()
            }
        }


        // Metodo para configurar un elemento como "arrastrable"
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

        // Metodo para configurar una silueta como "receptora"
        // Método para configurar una silueta como "receptora"
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

                        // Si la ficha arrastrada es "triangulo"
                        if (dragData == "triangulo") {
                            // Cambiar la imagen del receptor por "vela_circulo"
                            imageView.setImageResource(R.drawable.vela_circulo)
                            // Hacer que el "triángulo" desaparezca
                            triangulo.visibility = View.INVISIBLE
                        }

                        // Si la ficha arrastrada es "pegote"
                        if (dragData == "pegote") {
                            // Cambiar la imagen del receptor por "vela"
                            imageView.setImageResource(R.drawable.vela)
                            // Hacer que el "pegote" desaparezca
                            pegote.visibility = View.INVISIBLE
                            // Hacer visible el botón de "salir"
                            btnSalir.visibility = View.VISIBLE
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
