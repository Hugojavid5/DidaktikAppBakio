package com.icjardinapps.dm2.bakio.Txakolina

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.Mapa.Mapa
import com.icjardinapps.dm2.bakio.R
/**
 * Actividad que simula la cosecha y prensado de uvas para la elaboración del txakoli.
 * Se utiliza el drag-and-drop para mover objetos en pantalla (como las uvas y la caja),
 * y se hace un seguimiento de las acciones del usuario con toques y eventos.
 */
class ActividadTxakoli : AppCompatActivity() {

    private lateinit var ivVid: ImageView
    private lateinit var ivUva: ImageView
    private lateinit var ivPies: ImageView
    private lateinit var ivCaja: ImageView
    private lateinit var ivBarril: ImageView
    private lateinit var ivReloj: ImageView
    private lateinit var ivBotella: ImageView
    private lateinit var imageButtonNextActivity: ImageButton

    private var toques = 0
    private val maxToques = 10 // Número de toques para cambiar a la siguiente fase
    /**
     * Método llamado cuando se crea la actividad. Se inicializan las vistas, se configuran
     * los eventos de drag-and-drop y los clics de los elementos interactivos (como las uvas y la caja).
     */
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
        imageButtonNextActivity = findViewById(R.id.imageButtonNextActivity)
        imageButtonNextActivity.visibility = View.GONE // Ocultar el botón al inicio

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
                    ivPies.visibility = View.GONE
                    Toast.makeText(this,
                        getString(R.string.uvas_recogidas_y_llevadas_a_la_caja), Toast.LENGTH_SHORT).show()
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    if (!event.result) {
                        Toast.makeText(this,
                            getString(R.string.arrastra_las_uvas_hacia_los_pies_para_continuar), Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this,
                        getString(R.string.pisando_uvas_sigue_tocando), Toast.LENGTH_SHORT).show()
                } else {
                    // Cuando se den X toques, se muestra el barril
                    ivCaja.visibility = View.GONE
                    ivBarril.visibility = View.VISIBLE
                    ivReloj.visibility = View.VISIBLE
                    Toast.makeText(this,
                        getString(R.string.las_uvas_han_sido_prensadas_y_llevadas_al_barril), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this,
                    getString(R.string.primero_debes_recoger_las_uvas_y_llevarlas_a_la_caja), Toast.LENGTH_SHORT).show()
            }
        }

        ivReloj.setOnClickListener {
            if (ivBarril.visibility == View.VISIBLE) {
                // Si el barril es visible, pasa a la siguiente etapa
                ivReloj.isClickable = false
                ivBotella.visibility = View.VISIBLE // Mostrar la botella después de 1 año

                // Log para verificar que el flujo llegó aquí
                Log.d("ActividadTxakoli", "El barril es visible, mostrando la botella y el botón siguiente.")

                Toast.makeText(this,
                    getString(R.string.ha_pasado_1_a_o_el_txakoli_est_listo_para_embotellar), Toast.LENGTH_LONG).show()

                // Asegurarse de que el botón se hace visible
                imageButtonNextActivity.visibility = View.VISIBLE

                // Verificar la visibilidad del botón
                Log.d("ActividadTxakoli", "Botón siguiente visibilidad: ${imageButtonNextActivity.visibility}")
            } else {
                // Si no se cumplen las condiciones, mostrar un mensaje de advertencia
                Toast.makeText(this,
                    getString(R.string.primero_pisa_las_uvas_y_gu_rdalas_en_el_barril), Toast.LENGTH_SHORT).show()
            }
        }

        imageButtonNextActivity.setOnClickListener {
            val intent = Intent(this, Mapa::class.java)
            intent.putExtra("ACTUALIZAR_PUNTOS", true)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

    }
}
