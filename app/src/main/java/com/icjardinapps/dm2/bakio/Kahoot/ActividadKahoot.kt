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
        Question(getString(R.string.non_bizi_da_lanper), getString(R.string.udaletxean), listOf(getString(R.string.etxean), getString(R.string.udaletxean), getString(R.string.itsasoan), getString(R.string.kalean))),
        Question(getString(R.string.non_ikusi), getString(R.string.jaietan), listOf(getString(R.string.jaietan), getString(R.string.igerilekuan), getString(R.string.herriko_plazan), getString(R.string.lagunekin_kafea_hartzen))),
        Question(getString(R.string.zer_dago_gaztelugatxen), getString(R.string.ermita), listOf(getString(R.string.ermita), getString(R.string.altxorra), getString(R.string.taberna_bat), getString(R.string.lapurrak))),
        Question(getString(R.string.nondik_dator_txakolina), getString(R.string.mahatsetatik), listOf(getString(R.string.sagarretik), getString(R.string.mahatsetatik), getString(R.string.platanotik), getString(R.string.ahabietatik))),
        Question(getString(R.string.Txakolinaren_museoan), getString(R.string.gezurra), listOf(getString(R.string.egia), getString(R.string.gezurra))),
        Question(getString(R.string.zer_da_anarrua), getString(R.string.inauterietako_pretsonaia_bat), listOf(getString(R.string.mamu_bat), getString(R.string.inauterietako_pretsonaia_bat), getString(R.string.bakioko_alkatea))),
        Question(getString(R.string.zein_dantza_egiten_da_bakion), getString(R.string.anarruaren_zortzikoa), listOf(getString(R.string.anarruren_banakoa), getString(R.string.anarruaren_binakakoa), getString(R.string.anarruaren_launakakoa), getString(R.string.anarruaren_zortzikoa))),
        Question(getString(R.string.san_pelaio_ermita_harriz_eraikita_dago), getString(R.string.egia), listOf(getString(R.string.egia), getString(R.string.gezurra))),
        Question(getString(R.string.zenbat_neurtzen_du_matxitxakoko_itsasargiak), "20m", listOf("15m", "20m", "25m", "30m")),
        Question(getString(R.string.bakioko_bela_gurutzatuak_zer_isladatzen_du), getString(R.string.piraten_itsasontzia_ondoratu_zela), listOf(getString(R.string.piratak_bakiora_iritsi_ziren_eguna), getString(R.string.piratek_bakion_lapurtu_zuten_garaia), getString(R.string.piraten_itsasontzia_ondoratu_zela), getString(R.string.ez_dauka_esanahirik)))
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
                Toast.makeText(this,
                    getString(R.string.por_favor_selecciona_una_respuesta), Toast.LENGTH_SHORT).show()
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
