package com.icjardinapps.dm2.bakio.Kahoot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.ConexionBBDD.ConexionDb
import com.icjardinapps.dm2.bakio.Final.FinDelJuego
import com.icjardinapps.dm2.bakio.R

class FinKahoot : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_final_del_juego)

        // Obtener los datos pasados desde la actividad anterior
        val correctCount = intent.getIntExtra("correctCount", 0)
        val incorrectCount = intent.getIntExtra("incorrectCount", 0)
        val totalQuestions = correctCount + incorrectCount

        // Calcular el porcentaje de aciertos
        val percentage = if (totalQuestions > 0) {
            (correctCount.toDouble() / totalQuestions * 100).toInt()
        } else {
            0
        }

        // Mostrar los resultados
        val textViewResults = findViewById<TextView>(R.id.textViewResults)
        textViewResults.text = getString(R.string.total_aciertos) + correctCount + "\n" +
                getString(R.string.total_de_fallos) + incorrectCount + "\n" +
                getString(R.string.el_porcentaje_de_aciertos_es) + percentage + "%"


        // Mostrar la animación de sonrisa o tristeza
        val imageView = findViewById<ImageView>(R.id.imageViewAnimation)
        if (percentage >= 75) {
            imageView.setImageResource(R.drawable.sonrisa)  // Asegúrate de tener esta animación en tu carpeta drawable
        } else {
            imageView.setImageResource(R.drawable.triste)  // Asegúrate de tener esta animación en tu carpeta drawable
        }

        val buttonEndGame = findViewById<Button>(R.id.buttonEndGame)
        buttonEndGame.setOnClickListener {
            val bd = ConexionDb(this)
            val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
            val usuario = sharedPref.getString("username", "Invitado") ?: "Invitado"

            Log.i("DB_INFO", "Llamando a guardarPuntuacionNivel() con usuario: $usuario, puntuación: $percentage")

            Thread {
                val resultado = bd.guardarPuntuacionNivel(usuario, percentage)

                runOnUiThread {
                    if (resultado) {
                        Log.i("DB_INFO", "La puntuación se guardó correctamente en la BD.")
                    } else {
                        Log.e("DB_ERROR", "Error al guardar la puntuación.")
                    }
                    val intent = Intent(this, FinDelJuego::class.java)
                    startActivity(intent)
                    finish()
                }
            }.start()
        }

    }}
