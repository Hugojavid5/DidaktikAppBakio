package com.icjardinapps.dm2.bakio.Final

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R

/**
 * Actividad que representa la pantalla de fin del juego.
 * Muestra un mensaje y una animación con un retraso.
 */
class FinDelJuego : AppCompatActivity() {

     /**
     * Método que se ejecuta cuando se crea la actividad.
     * Configura la interfaz de usuario y gestiona la animación de la imagen.
     *
     * @param savedInstanceState Estado guardado de la actividad (si existe).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_app)

        val tvFinDelJuego: TextView = findViewById(R.id.tvFinDelJuego)
        val ivAnimacion: ImageView = findViewById(R.id.ivAnimacion)

        // Mostrar la imagen después de un retraso
        Handler().postDelayed({
            ivAnimacion.setImageResource(R.drawable.sonrisa) // Cambiar a la imagen deseada
            ivAnimacion.visibility = ImageView.VISIBLE
        }, 2000)
    }
     /**
     * Sobrescribe el comportamiento del botón "Atrás" para evitar que el usuario retroceda.
     */
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // No hacemos nada, por lo que no se realizará ninguna acción al presionar la flecha de retroceso
    }
}
