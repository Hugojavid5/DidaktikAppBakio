package com.icjardinapps.dm2.bakio.Portada

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.icjardinapps.dm2.bakio.R

class PortadaDeLaApp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_portada)

        val buttonJugar = findViewById<Button>(R.id.buttonJugar)

        // Acción para el botón "Jugar"
        buttonJugar.setOnClickListener {
            //Redirige a la clase Bienvenida App
            val intent = Intent(this, BienvenidaApp::class.java)
            startActivity(intent)
        }
    }
}
