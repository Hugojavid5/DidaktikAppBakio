package com.icjardinapps.dm2.bakio.Portada

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.Bienvenida.BienvenidaApp
import com.icjardinapps.dm2.bakio.R
import java.util.Locale

class PortadaDeLaApp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Aplicar el idioma guardado antes de cargar la UI
        setAppLocale(getSavedLanguage(this))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_portada)

        // Configurar eventos de clic en la interfaz
        findViewById<View>(R.id.linearLayout).setOnClickListener {
            startActivity(Intent(this, BienvenidaApp::class.java))
        }

        findViewById<View>(R.id.imageView3).setOnClickListener {
            startActivity(Intent(this, BienvenidaApp::class.java))
        }

        findViewById<View>(R.id.imageView1).setOnClickListener {
            showLanguageDialog()
        }

        findViewById<View>(R.id.imageView4).setOnClickListener {
            showInfoDialog()
        }

        findViewById<View>(R.id.imageView2).setOnClickListener {
            // Acción para ranking (puedes completar esto)
        }
    }

    // Método para mostrar el diálogo de selección de idioma
    private fun showLanguageDialog() {
        val languages = arrayOf("Español", "English", "Euskera")
        val languageCodes = arrayOf("es", "en", "eu")
        val flagImages = arrayOf(R.drawable.flag_spain, R.drawable.flag_uk, R.drawable.flag_basque)

        val adapter = object : ArrayAdapter<String>(this, R.layout.list_item_language, languages) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: layoutInflater.inflate(R.layout.list_item_language, parent, false)

                val imageView = view.findViewById<ImageView>(R.id.languageIcon)
                val textView = view.findViewById<TextView>(R.id.languageName)

                imageView.setImageResource(flagImages[position]) // Asignar imagen de bandera
                textView.text = languages[position] // Asignar nombre del idioma

                return view
            }
        }

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.selecciona_el_idioma))
            .setAdapter(adapter) { _, which ->
                setAppLocale(languageCodes[which])
                recreate() // Recargar la actividad para aplicar los cambios de idioma
            }
            .show()
    }


    // Método para cambiar el idioma en toda la aplicación y guardarlo en SharedPreferences
    private fun setAppLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Guardar el idioma seleccionado en SharedPreferences
        saveLanguage(languageCode)

        // Mostrar mensaje de confirmación
        Toast.makeText(this, getString(R.string.idioma_cambiado), Toast.LENGTH_SHORT).show()
    }

    // Guardar el idioma en SharedPreferences
    private fun saveLanguage(languageCode: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("language", languageCode).apply()
    }

    // Obtener el idioma guardado en SharedPreferences
    private fun getSavedLanguage(context: Context): String {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("language", Locale.getDefault().language) ?: "es"
    }

    // Método para mostrar información de los desarrolladores
    private fun showInfoDialog() {
        val names = arrayOf("Beñat Cano", "Hugo Javid", "Guillermo Arana")

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.info_desarro))
            .setMessage(names.joinToString("\n"))
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
