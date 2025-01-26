package com.icjardinapps.dm2.bakio.Portada

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.Bienvenida.BienvenidaApp
import com.icjardinapps.dm2.bakio.R
import java.util.Locale

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

        // Configurar el evento de clic para la imagen de "play"
        val imageViewPlay = findViewById<View>(R.id.imageView3)
        imageViewPlay.setOnClickListener {
            // Redirigir a la actividad BienvenidaApp cuando se pulse la imagen de play
            val intent = Intent(this, BienvenidaApp::class.java)
            startActivity(intent)
        }

        // Configurar el evento de clic para la imagen de ajustes (cambiar idioma)
        val imageViewAjustes = findViewById<View>(R.id.imageView1)
        imageViewAjustes.setOnClickListener {
            // Mostrar un diálogo para elegir el idioma
            showLanguageDialog()
        }

        // Configurar el evento de clic para la imagen de "información" (mostrar nombres)
        val imageViewInfo = findViewById<View>(R.id.imageView4)
        imageViewInfo.setOnClickListener {
            // Mostrar un diálogo con los nombres cuando se pulse la imagen de info
            showInfoDialog()
        }

        // Otros eventos de clic para las imágenes
        val imageViewRanking = findViewById<View>(R.id.imageView2)
        imageViewRanking.setOnClickListener {
            // Acción para la imagen de ranking
        }
    }

    // Metodo para mostrar el diálogo de selección de idioma
    private fun showLanguageDialog() {
        val languages = arrayOf("Español", "English", "Euskara")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona el idioma")
        builder.setItems(languages) { _, which ->
            when (which) {
                0 -> setLocale("es")  // Español
                1 -> setLocale("en")  // Inglés
                2 -> setLocale("eu")  // Euskera
            }
        }
        builder.show()
    }

    // Método para cambiar el idioma de la aplicación
    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)

        // Guardar la preferencia de idioma en SharedPreferences (opcional)
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        sharedPreferences.edit().putString("language", languageCode).apply()

        // Recargar la actividad para aplicar el cambio de idioma
        recreate()

        // Mostrar un Toast indicando que el idioma se cambió correctamente
        Toast.makeText(this, "Idioma cambiado correctamente", Toast.LENGTH_SHORT).show()
    }

    // Metodo para mostrar un diálogo con nombres de prueba
    private fun showInfoDialog() {
        val names = arrayOf("Beñat Cano", "Hugo Javid", "Guillermo Arana")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Información de los desarrolladores")
        builder.setMessage(names.joinToString("\n"))  // Muestra los nombres en el mensaje
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()  // Solo cerrar el diálogo al pulsar OK
        }
        builder.show()
    }
}