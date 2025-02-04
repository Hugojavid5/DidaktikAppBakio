package com.icjardinapps.dm2.bakio.Final

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R


class FinDelJuego : AppCompatActivity() {
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
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // No hacemos nada, por lo que no se realizará ninguna acción al presionar la flecha de retroceso
    }
}
