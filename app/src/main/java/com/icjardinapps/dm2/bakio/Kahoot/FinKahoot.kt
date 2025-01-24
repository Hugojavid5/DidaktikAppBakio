package com.icjardinapps.dm2.bakio.Kahoot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
        textViewResults.text = """
            Total aciertos: $correctCount
            Total de fallos: $incorrectCount
            El porcentaje de aciertos es: $percentage%
        """

        // Mostrar la animación de sonrisa o tristeza
        val imageView = findViewById<ImageView>(R.id.imageViewAnimation)
        if (percentage >= 75) {
            imageView.setImageResource(R.drawable.sonrisa)  // Asegúrate de tener esta animación en tu carpeta drawable
        } else {
            imageView.setImageResource(R.drawable.triste)  // Asegúrate de tener esta animación en tu carpeta drawable
        }

        // Configurar el botón 'Fin del juego'
        val buttonEndGame = findViewById<Button>(R.id.buttonEndGame)
        buttonEndGame.setOnClickListener {
            // Redirigir a otra actividad (por ejemplo, MainActivity)
            val intent = Intent(this, FinDelJuego::class.java)
            startActivity(intent)
            finish() // Finalizar la actividad actual
        }
    }
}
