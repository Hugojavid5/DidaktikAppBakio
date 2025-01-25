package com.icjardinapps.dm2.bakio.Login

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.Portada.PortadaDeLaApp
import com.icjardinapps.dm2.bakio.R

class Registro : AppCompatActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var contrasenaEditText: EditText
    private lateinit var registrarButton: Button
    private lateinit var loadingAnimation: ImageView
    private lateinit var animationDrawable: AnimationDrawable
    private lateinit var tituloRegistro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Inicializamos los componentes
        nombreEditText = findViewById(R.id.editTextNombre)
        contrasenaEditText = findViewById(R.id.editTextContrasena)
        registrarButton = findViewById(R.id.buttonRegistrar)
        loadingAnimation = findViewById(R.id.loadingAnimation)
        tituloRegistro = findViewById(R.id.tituloRegistro)

        // Cargar la animación desde el archivo XML (solo una vez)
        loadingAnimation.setBackgroundResource(R.drawable.sonrisa) // Establece la animación
        animationDrawable = loadingAnimation.background as AnimationDrawable

        // Iniciar la animación
        animationDrawable.start()

        // Validar campos
        registrarButton.isEnabled = false
        nombreEditText.addTextChangedListener(textWatcher)
        contrasenaEditText.addTextChangedListener(textWatcher)

        // Acción del botón de registrar
        registrarButton.setOnClickListener {
            // Aquí puedes agregar la lógica de registro

            // Simular un retraso antes de redirigir
            val intent = Intent(this, PortadaDeLaApp::class.java)
            startActivity(intent)
            finish()  // Finaliza la actividad actual
        }
    }

    private fun validarCampos() {
        val nombre = nombreEditText.text.toString()
        val contrasena = contrasenaEditText.text.toString()
        registrarButton.isEnabled = nombre.isNotEmpty() && contrasena.isNotEmpty()
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            validarCampos()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}
