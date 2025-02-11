// Registro.kt
package com.icjardinapps.dm2.bakio.Login

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.Portada.PortadaDeLaApp
import com.icjardinapps.dm2.bakio.R
import com.icjardinapps.dm2.bakio.ConexionBBDD.ConexionDb
/**
 * Actividad que maneja el proceso de registro de un usuario en la aplicación.
 * Permite al usuario ingresar su nombre y contraseña, validando los campos antes de proceder
 * y registrando al usuario en la base de datos.
 */
class Registro : AppCompatActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var contrasenaEditText: EditText
    private lateinit var registrarButton: Button
    private lateinit var loadingAnimation: ImageView
    private lateinit var animationDrawable: AnimationDrawable
    private lateinit var tituloRegistro: TextView
/**
     * Método que se ejecuta al crear la actividad.
     * Inicializa los componentes de la interfaz de usuario, configura la animación de carga,
     * y valida los campos del formulario de registro.
     * Además, maneja el proceso de registro del usuario en la base de datos.
     *
     * @param savedInstanceState Estado guardado de la actividad (si existe).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("INFO", "INICIO APLICACIÓN")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Inicializamos los componentes
        nombreEditText = findViewById(R.id.editTextNombre)
        contrasenaEditText = findViewById(R.id.editTextContrasena)
        registrarButton = findViewById(R.id.buttonRegistrar)
        loadingAnimation = findViewById(R.id.loadingAnimation)
        tituloRegistro = findViewById(R.id.tituloRegistro)

        // Cargar la animación desde el archivo XML (solo una vez)
        loadingAnimation.setBackgroundResource(R.drawable.sonrisa)
        animationDrawable = loadingAnimation.background as AnimationDrawable

        // Iniciar la animación
        animationDrawable.start()

        // Validar campos
        registrarButton.isEnabled = false
        nombreEditText.addTextChangedListener(textWatcher)

        // Acción del botón de registrar
        registrarButton.setOnClickListener {
            // Validamos los campos antes de continuar
            val nombre = nombreEditText.text.toString()
            val contrasena = contrasenaEditText.text.toString()

            if (nombre.isNotEmpty()) {
                // Hacemos la inserción en base de datos en un hilo
                Thread {
                    val bd = ConexionDb(this)
                    val insertSuccess = bd.guardarAlumnoBBDD(nombre, nombre, 3)

                    // Si la inserción es exitosa, redirigimos a la portada
                    runOnUiThread {
                        if (insertSuccess) {
                            val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
                            val editor = sharedPref.edit()
                            editor.putString("username", nombre)
                            editor.apply()

                            // Redirigir a la portada de la app
                            val intent = Intent(this, PortadaDeLaApp::class.java)
                            startActivity(intent)
                            finish()  // Finaliza la actividad actual
                        } else {
                            // Si no se pudo insertar, mostramos un mensaje de error
                            Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.start()
            } else {
                // Si los campos no están completos, mostramos un mensaje
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
    /**
     * Método para validar si los campos del formulario están completos.
     * Habilita o deshabilita el botón de registrar dependiendo si el nombre es válido.
     */
    private fun validarCampos() {
        val nombre = nombreEditText.text.toString()
        registrarButton.isEnabled = nombre.isNotEmpty()
    }
     /**
     * Un TextWatcher para validar los campos en tiempo real mientras el usuario escribe.
     * Habilita o deshabilita el botón de registrar dependiendo de si el nombre está vacío o no.
     */
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            validarCampos()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}
