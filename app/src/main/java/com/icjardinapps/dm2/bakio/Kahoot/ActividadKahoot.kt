package com.icjardinapps.dm2.bakio.Kahoot

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.icjardinapps.dm2.bakio.R


class ActividadKahoot : AppCompatActivity() {

    private var currentQuestionIndex = 0
    private val userAnswers = MutableList<String?>(10) { null }
    private val questions = listOf(
        Question("Non bizi da Lanper?", "Udaletxean", listOf("Etxean", "Udaletxean", "Itsasoan", "Kalean")),
        Question("Non ikusi dezakegu Lanper?", "Jaietan", listOf("Jaietan", "Igerilekuan", "Herriko plazan", "Lagunekin kafea hartzen")),
        Question("Zer dago Gaztelugatxen?", "Ermita", listOf("Ermita", "Altxorra", "Taberna bat", "Lapurrak")),
        Question("Nondik dator txakolina?", "Mahatsetatik", listOf("Sagarretik", "Mahatsetatik", "Platanotik", "Ahabietatik")),
        Question("Txakolinaren museoan txakolina egin eta eraten da?", "Gezurra", listOf("Egia", "Gezurra")),
        Question("Zer da Anarrua?", "Inauterietako pretsonaia bat", listOf("Mamu bat", "Inauterietako pretsonaia bat", "Bakioko alkatea")),
        Question("Zein dantza egiten da Bakion?", "Anarruaren zortzikoa", listOf("Anarruren banakoa", "Anarruaren binakakoa", "Anarruaren launakakoa", "Anarruaren zortzikoa")),
        Question("San Pelaio ermita harriz eraikita dago?", "Egia", listOf("Egia", "Gezurra")),
        Question("Zenbat neurtzen du Matxitxakoko itsasargiak?", "20m", listOf("15m", "20m", "25m", "30m")),
        Question("Bakioko bela gurutzatuak zer isladatzen du?", "Piraten itsasontzia ondoratu zela", listOf("Piratak Bakiora iritsi ziren eguna", "Piratek Bakion lapurtu zuten garaia", "Piraten itsasontzia ondoratu zela", "Ez dauka esanahirik"))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kahoot)

        val questionTextView = findViewById<TextView>(R.id.textViewQuestion)
        val radioGroupAnswers = findViewById<RadioGroup>(R.id.radioGroupAnswers)
        val nextQuestionButton = findViewById<Button>(R.id.buttonNextQuestion)
        val previousQuestionButton = findViewById<Button>(R.id.buttonPreviousQuestion)
        val backToWelcomeButton = findViewById<Button>(R.id.buttonBackToWelcome)

        showQuestion(questionTextView, radioGroupAnswers)
        updateButtonVisibility()

        nextQuestionButton.setOnClickListener {
            val selectedOptionId = radioGroupAnswers.checkedRadioButtonId
            if (selectedOptionId == -1) {
                Toast.makeText(this, "Por favor, selecciona una respuesta.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedAnswer = findViewById<RadioButton>(selectedOptionId).text.toString()

            // Guardar la respuesta de la pregunta actual
            userAnswers[currentQuestionIndex] = selectedAnswer

            // Verificar si hay más preguntas, si no, mostrar los resultados
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                radioGroupAnswers.clearCheck()
                showQuestion(questionTextView, radioGroupAnswers)
            } else {
                showResults() // Mostrar los resultados después de la última pregunta
            }

            updateButtonVisibility()
        }
        previousQuestionButton.setOnClickListener {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                showQuestion(questionTextView, radioGroupAnswers)
                updateButtonVisibility()
                val savedAnswer = userAnswers.getOrNull(currentQuestionIndex)
                radioGroupAnswers.clearCheck()
                if (savedAnswer != null) {
                    for (i in 0 until radioGroupAnswers.childCount) {
                        val radioButton = radioGroupAnswers.getChildAt(i) as RadioButton
                        if (radioButton.text == savedAnswer) {
                            radioButton.isChecked = true
                            break
                        }
                    }
                }
            }
        }

        backToWelcomeButton.setOnClickListener {
            startActivity(Intent(this, ActividadBienvenidaKahoot::class.java))
            finish()
        }
    }

    private fun showQuestion(questionTextView: TextView, radioGroupAnswers: RadioGroup) {
        val question = questions[currentQuestionIndex]
        questionTextView.text = question.text

        radioGroupAnswers.removeAllViews()
        question.answers.forEach { answer ->
            val radioButton = RadioButton(this)
            radioButton.text = answer
            radioGroupAnswers.addView(radioButton)
        }
    }
    private fun updateButtonVisibility() {
        val previousQuestionButton = findViewById<Button>(R.id.buttonPreviousQuestion)
        val nextQuestionButton = findViewById<Button>(R.id.buttonNextQuestion)
        val checkAnswersButton = findViewById<Button>(R.id.buttonCheckAnswers)
        val backToWelcomeButton = findViewById<Button>(R.id.buttonBackToWelcome)

        // Inicialmente deshabilitar todos los botones
        previousQuestionButton.isEnabled = false
        checkAnswersButton.isEnabled = false
        nextQuestionButton.isEnabled = true
        backToWelcomeButton.isEnabled = true

        // Lógica de visibilidad de botones según la pregunta actual
        when (currentQuestionIndex) {
            0 -> {
                previousQuestionButton.isEnabled = false // No hay pregunta anterior
                backToWelcomeButton.isEnabled = true // Habilitar botón "Volver" solo en la primera pregunta
            }
            questions.size - 1 -> {
                nextQuestionButton.isEnabled = false // No hay siguiente pregunta
                checkAnswersButton.isEnabled = true // Mostrar el botón de comprobar respuestas en la última pregunta
                previousQuestionButton.isEnabled = true // Habilitar el botón de pregunta anterior en la última pregunta
                backToWelcomeButton.isEnabled = false // Deshabilitar el botón "Volver" en la última pregunta
            }
            else -> {
                previousQuestionButton.isEnabled = true // Habilitar el botón de pregunta anterior
                nextQuestionButton.isEnabled = true // Habilitar el botón de siguiente pregunta
                checkAnswersButton.isEnabled = false // Deshabilitar el botón de comprobar respuestas
                backToWelcomeButton.isEnabled = false // Deshabilitar el botón "Volver" en las preguntas intermedias
            }
        }

        // Establecer el OnClickListener para el botón de comprobar respuestas
        checkAnswersButton.setOnClickListener {
            // Llamar a la función para mostrar los resultados
            showResults()
        }
    }

    private fun showResults() {
        // Cambiar el layout a activity_respuestas.xml
        setContentView(R.layout.activity_respuestas)

        // Obtener el LinearLayout donde mostrar las respuestas
        val linearLayoutAnswers = findViewById<LinearLayout>(R.id.linearLayoutAnswers)
        val correctAnswersButton = findViewById<Button>(R.id.buttonCorrectAnswers)

        // Limpiar cualquier vista previa de respuestas
        linearLayoutAnswers.removeAllViews()

        // Mostrar las respuestas seleccionadas por el usuario
        userAnswers.forEachIndexed { index, answer ->
            val question = questions[index]
            val displayedAnswer = answer ?: "${question.correctAnswer}"
            val answerTextView = TextView(this).apply {
                text = "Pregunta ${index + 1}: $displayedAnswer"
                textSize = 18f
                setPadding(0, 8, 0, 8)  // Agregar algo de espacio entre las respuestas
            }
            linearLayoutAnswers.addView(answerTextView)
        }

        // Lógica al hacer clic en el botón de "Corregir"
        correctAnswersButton.setOnClickListener {
            // Contar las respuestas correctas e incorrectas
            val correctCount = userAnswers.filterIndexed { index, answer ->
                answer == questions[index].correctAnswer
            }.count()
            val incorrectCount = userAnswers.size - correctCount

            // Redirigir a la pantalla de resultados finales
            val intent = Intent(this, FinKahoot::class.java).apply {
                putExtra("correctCount", correctCount)
                putExtra("incorrectCount", incorrectCount)
            }
            startActivity(intent)
            finish()  // Finalizar la actividad actual para no volver atrás
        }
    }

    data class Question(val text: String, val correctAnswer: String, val answers: List<String>)
}
