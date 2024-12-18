package com.icjardinapps.dm2.bakio.Txakolina

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R

class ActividadTxakoli : AppCompatActivity() {

    private lateinit var ivVid: ImageView
    private lateinit var ivUva: ImageView
    private lateinit var ivPies: ImageView
    private lateinit var ivCaja: ImageView
    private lateinit var ivBarril: ImageView
    private lateinit var ivReloj: ImageView
    private lateinit var ivBotella: ImageView
    private lateinit var btnVolverMapa: Button

    private var toques = 0
    private val maxToques = 10 // Número de toques para cambiar a la siguiente fase

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_txakoli)

        // Inicialización de las vistas
        ivVid = findViewById(R.id.iv_vid)
        ivUva = findViewById(R.id.iv_uva)
        ivPies = findViewById(R.id.iv_pies)
        ivCaja = findViewById(R.id.iv_caja)
        ivBarril = findViewById(R.id.iv_barril)
        ivReloj = findViewById(R.id.iv_reloj)
        ivBotella = findViewById(R.id.iv_botella)
        btnVolverMapa = findViewById(R.id.btn_volver_mapa)
        btnVolverMapa.visibility = View.GONE // Ocultar el botón al inicio

        // Hacer que la uva sea arrastrable
        ivUva.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val dragShadow = View.DragShadowBuilder(view)
                view.startDragAndDrop(null, dragShadow, view, 0)
                true
            } else {
                false
            }
        }

        // Hacer que los pies reciban la uva arrastrada
        ivPies.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    // Si se arrastra correctamente, realizar la acción
                    ivVid.visibility = View.GONE // Eliminar la vid
                    ivCaja.visibility = View.VISIBLE // Mostrar la caja
                    ivUva.visibility = View.GONE // La uva desaparece de la vid
                    ivPies.visibility=View.GONE
                    Toast.makeText(this, "¡Uvas recogidas y llevadas a la caja!", Toast.LENGTH_SHORT).show()
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    if (!event.result) {
                        Toast.makeText(this, "Arrastra las uvas hacia los pies para continuar.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            true
        }

        // Manejar toques en la caja
        ivCaja.setOnClickListener {
            if (ivCaja.visibility == View.VISIBLE) {
                toques++
                if (toques < maxToques) {
                    Toast.makeText(this, "¡Pisando uvas! Sigue tocando...", Toast.LENGTH_SHORT).show()
                } else {
                    // Cuando se den X toques, se muestra el barril
                    ivCaja.visibility = View.GONE
                    ivBarril.visibility = View.VISIBLE
                    ivReloj.visibility = View.VISIBLE
                    Toast.makeText(this, "Las uvas han sido prensadas y llevadas al barril.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Primero debes recoger las uvas y llevarlas a la caja!", Toast.LENGTH_SHORT).show()
            }
        }

        // Manejar clic en el reloj para simular el paso de un año
        ivReloj.setOnClickListener {
            if (ivBarril.visibility == View.VISIBLE) {
                ivReloj.setImageResource(R.drawable.reloj) // Cambiar la imagen si es necesario
                ivReloj.isClickable = false
                ivBotella.visibility = View.VISIBLE // Mostrar la botella después de 1 año
                Toast.makeText(this, "Ha pasado 1 año. El txakoli está listo para embotellar.", Toast.LENGTH_LONG).show()
                btnVolverMapa.visibility = View.VISIBLE // Mostrar el botón para volver al mapa
            } else {
                Toast.makeText(this, "¡Primero pisa las uvas y guárdalas en el barril!", Toast.LENGTH_SHORT).show()
            }
        }
    /*
        // Manejar clic en el botón para volver al mapa
        btnVolverMapa.setOnClickListener {
            val intent = Intent(this, Mapa::class.java)
            startActivity(intent)
            finish()
        }

 */
    }
}
