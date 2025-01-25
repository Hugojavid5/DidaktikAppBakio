package com.icjardinapps.dm2.bakio.Sopa

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.view.MotionEvent
import android.widget.Button
import com.icjardinapps.dm2.bakio.Mapa.Mapa
import com.icjardinapps.dm2.bakio.R
import kotlin.random.Random

class ActividadSopaDeLetras : AppCompatActivity() {

    private lateinit var buttonNext: Button
    private var lastSelectedRow: Int? = null
    private var lastSelectedCol: Int? = null
    private val foundWords = mutableSetOf<String>()
    private val cellWordsMap = mutableMapOf<Pair<Int, Int>, MutableList<String>>()
    private val gridSize = 10

    private val words =
        listOf("LANPER", "UDALETXEA", "BAKIO", "DOLOZA", "ITSASOA", "ARRANTZA", "JAIAK")
    private lateinit var gridLayout: GridLayout
    private lateinit var textViewClues: TextView
    private var selectedWord = StringBuilder()
    private lateinit var wordSearchLetters: Array<Array<Char>>
    private val selectedCells = mutableListOf<Pair<Int, Int>>() // Para almacenar las celdas seleccionadas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sopa)

        gridLayout = findViewById(R.id.grid_sopa_de_letras)
        buttonNext = findViewById(R.id.button_next)
        textViewClues = findViewById(R.id.textViewClues)

        // Inicialmente ocultamos el botón
        buttonNext.visibility = Button.INVISIBLE

        wordSearchLetters = generateRandomWordSearch()
        setupWordSearchGrid()
        updateCluesDisplay()

        buttonNext.setOnClickListener {
            // Aquí pasas las coordenadas del nuevo punto a la actividad de mapa
            val latitude = 43.4116  // Ejemplo de latitud
            val longitude = -2.7737  // Ejemplo de longitud

            val intent = Intent(this, Mapa::class.java).apply {
                putExtra("latitude", latitude)
                putExtra("longitude", longitude)
            }
            startActivity(intent)
        }
        buttonNext.isEnabled = false

        setupTouchListener()
    }

    private fun setupTouchListener() {
        gridLayout.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    handleTouchStart(event)
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    handleTouchMove(event)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    handleTouchEnd()
                    true
                }

                else -> false
            }
        }
    }

    private fun handleTouchStart(event: MotionEvent) {
        val (row, col) = getTouchedCell(event) ?: return
        resetSelection()
        selectCell(row, col)
    }

    private fun handleTouchMove(event: MotionEvent) {
        val (row, col) = getTouchedCell(event) ?: return
        if (row != lastSelectedRow || col != lastSelectedCol) {
            selectCell(row, col)
        }
    }

    private fun handleTouchEnd() {
        checkWord()
        resetSelection() // Solo se resetea después de comprobar si la palabra es correcta o no
    }

    private fun getTouchedCell(event: MotionEvent): Pair<Int, Int>? {
        val x = event.x
        val y = event.y
        val cellWidth = gridLayout.width / gridSize
        val cellHeight = gridLayout.height / gridSize

        val col = (x / cellWidth).toInt()
        val row = (y / cellHeight).toInt()

        return if (row in 0 until gridSize && col in 0 until gridSize) {
            Pair(row, col)
        } else {
            null
        }
    }

    private fun selectCell(row: Int, col: Int) {
        val linearIndex = row * gridSize + col
        val textView = gridLayout.getChildAt(linearIndex) as TextView

        // Permitir seleccionar celdas ya validadas (en verde)
        if (!selectedCells.contains(Pair(row, col))) {
            selectedCells.add(Pair(row, col))
            selectedWord.append(textView.text)

            // Cambiar el color solo si la celda no está en verde
            if (!cellWordsMap.containsKey(Pair(row, col))) {
                textView.setBackgroundColor(Color.YELLOW)
            }
        }
    }




    private fun resetSelection() {
        for ((row, col) in selectedCells) {
            val linearIndex = row * gridSize + col
            val textView = gridLayout.getChildAt(linearIndex) as TextView

            // Si la celda forma parte de palabras correctas, mantener en verde
            if (cellWordsMap.containsKey(Pair(row, col))) {
                textView.setBackgroundColor(Color.GREEN)
            } else {
                // Restablecer solo celdas temporalmente seleccionadas
                textView.setBackgroundColor(Color.TRANSPARENT)
            }
        }
        selectedWord.clear()
        selectedCells.clear()
        lastSelectedRow = null
        lastSelectedCol = null
    }






    private fun isCorrectWord(): Boolean {
        return selectedWord.toString() in words
    }

    private fun checkWord() {
        val currentWord = selectedWord.toString()

        if (isCorrectWord() && currentWord !in foundWords) {
            foundWords.add(currentWord)
            updateCluesDisplay()

            // Marcar celdas como correctas (verde) y añadir al mapa de palabras
            for ((row, col) in selectedCells) {
                val linearIndex = row * gridSize + col
                val textView = gridLayout.getChildAt(linearIndex) as TextView

                // Añadir palabra al mapa de celdas
                cellWordsMap.computeIfAbsent(Pair(row, col)) { mutableListOf() }
                cellWordsMap[Pair(row, col)]!!.add(currentWord)

                // Marcar como verde
                textView.setBackgroundColor(Color.GREEN)
            }

            if (foundWords.size == words.size) {
                buttonNext.isEnabled = true
                buttonNext.visibility = Button.VISIBLE
            }
        } else {
            resetSelection()
        }
    }






    private fun updateCluesDisplay() {
        val cluesText = words.joinToString("\n") { word ->
            if (word in foundWords) "$word ✓" else word
        }
        textViewClues.text = cluesText
    }

    private fun showCompletionMessage() {
        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle("¡Felicidades!")
            .setMessage("Has encontrado todas las palabras.")
            .setPositiveButton("OK", null)
            .create()
        dialog.show()
    }

    private fun placeWordInGrid(
        grid: Array<Array<Char>>, word: String,
        startRow: Int, startCol: Int, horizontal: Boolean
    ): Boolean {
        val rowDir = if (horizontal) 0 else 1 // Si es horizontal, no cambiar filas
        val colDir = if (horizontal) 1 else 0 // Si es vertical, no cambiar columnas

        var row = startRow
        var col = startCol

        // Verificar si la palabra cabe en la dirección seleccionada
        for (char in word) {
            if (row !in 0 until gridSize || col !in 0 until gridSize || (grid[row][col] != ' ' && grid[row][col] != char)) {
                return false
            }
            row += rowDir
            col += colDir
        }

        // Colocar la palabra
        row = startRow
        col = startCol
        for (char in word) {
            grid[row][col] = char
            row += rowDir
            col += colDir
        }

        return true
    }

    private fun generateRandomWordSearch(): Array<Array<Char>> {
        val grid = Array(gridSize) { Array(gridSize) { ' ' } }

        for (word in words) {
            var placed = false
            while (!placed) {
                val startRow = Random.nextInt(gridSize)
                val startCol = Random.nextInt(gridSize)
                val horizontal = Random.nextBoolean() // Aleatoriamente horizontal o vertical

                placed = placeWordInGrid(grid, word, startRow, startCol, horizontal)
            }
        }

        // Rellenar los espacios vacíos con letras aleatorias
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (grid[i][j] == ' ') {
                    grid[i][j] = ('A'..'Z').random()
                }
            }
        }

        return grid
    }

    private fun setupWordSearchGrid() {
        gridLayout.rowCount = gridSize
        gridLayout.columnCount = gridSize

        for (i in wordSearchLetters.indices) {
            for (j in wordSearchLetters[i].indices) {
                val textView = TextView(this).apply {
                    layoutParams = GridLayout.LayoutParams(
                        GridLayout.spec(i, 1f),
                        GridLayout.spec(j, 1f)
                    ).apply {
                        width = 0
                        height = GridLayout.LayoutParams.WRAP_CONTENT
                        setMargins(4, 4, 4, 4)
                    }
                    text = wordSearchLetters[i][j].toString()
                    textSize = 24f
                    setTextColor(Color.BLACK)
                    gravity = Gravity.CENTER
                    setBackgroundColor(Color.TRANSPARENT)
                }

                gridLayout.addView(textView)
            }
        }
    }

    private fun changeSelectedCellsColor(color: Int) {
        // Cambiar el color de fondo de las celdas seleccionadas a un color uniforme
        for ((row, col) in selectedCells) {
            val linearIndex = row * gridSize + col
            val textView = gridLayout.getChildAt(linearIndex) as TextView
            textView.setBackgroundColor(color)
        }
    }
}