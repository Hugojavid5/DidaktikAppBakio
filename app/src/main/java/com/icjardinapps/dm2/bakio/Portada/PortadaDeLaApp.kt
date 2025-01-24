package com.icjardinapps.dm2.bakio.Portada

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.icjardinapps.dm2.bakio.Bienvenida.BienvenidaApp
import com.icjardinapps.dm2.bakio.R

class PortadaDeLaApp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_portada)

        // Configurar el evento táctil para el LinearLayout
        val mainLayout = findViewById<View>(R.id.linearLayout)
        mainLayout.setOnClickListener {
            // Cambiar a otra actividad
            val intent = Intent(this, BienvenidaApp::class.java)
            startActivity(intent)
        }
    }
}
