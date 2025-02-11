package com.icjardinapps.dm2.bakio.Portada

import android.annotation.SuppressLint
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
import com.icjardinapps.dm2.bakio.Ranking.Ranking
import java.util.Locale
/**
 * Actividad principal de la aplicación, la cual actúa como la pantalla de portada.
 * Esta actividad permite al usuario navegar a diferentes secciones, como la bienvenida, selección de idioma, y ver el ranking.
 * También permite cambiar el idioma de la aplicación y muestra información sobre los desarrolladores.
 */
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
            startActivity(Intent(this, Ranking::class.java))
        }
    }

     /**
     * Muestra un diálogo para seleccionar el idioma de la aplicación.
     * Los usuarios pueden elegir entre Español, Inglés y Euskera.
     */
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

    /**
     * Cambia el idioma de la aplicación y lo guarda en SharedPreferences.
     *
     * @param languageCode El código del idioma (por ejemplo, "es" para español).
     */
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

     /**
     * Guarda el idioma seleccionado en SharedPreferences.
     *
     * @param languageCode El código del idioma que se guardará.
     */
    private fun saveLanguage(languageCode: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("language", languageCode).apply()
    }

  /**
     * Obtiene el idioma guardado en SharedPreferences. Si no se ha guardado ninguno, se usa el idioma por defecto del sistema.
     *
     * @param context El contexto de la aplicación.
     * @return El código de idioma guardado o el predeterminado del sistema.
     */
    private fun getSavedLanguage(context: Context): String {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("language", Locale.getDefault().language) ?: "es"
    }

   /**
     * Muestra un diálogo con información sobre los desarrolladores de la aplicación.
     */
    private fun showInfoDialog() {
        val names = arrayOf("DESARROLLADORES:","","-Beñat Cano", "-Hugo Javid", "-Guillermo Arana","","IDEA:","","-Saioa Uribe","-Libe Diaz de Argandoña","-Irati Arcelus"+"-Lorea Mariscal","-Izaro Etxebarria")

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.info_desarro))
            .setMessage(names.joinToString("\n"))
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
    /**
     * Evita que la acción predeterminada de retroceso se ejecute. La actividad no responderá al botón de retroceso.
     */
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // No hacemos nada, por lo que no se realizará ninguna acción al presionar la flecha de retroceso
    }
}
