package com.icjardinapps.dm2.bakio.Sopa

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.view.MotionEvent
import com.icjardinapps.dm2.bakio.Mapa.Mapa
import com.icjardinapps.dm2.bakio.R
import kotlin.random.Random
/**
 * Actividad que maneja la lógica de una sopa de letras, donde los usuarios deben encontrar palabras dentro de una cuadrícula de letras.
 * Esta actividad permite a los usuarios seleccionar letras de la cuadrícula para formar palabras, verificar si son correctas,
 * y ver pistas sobre las palabras encontradas.
 */
class ActividadSopaDeLetras : AppCompatActivity() {

    private lateinit var buttonNext: ImageButton
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

    /**
     * Método que se llama cuando la actividad es creada.
     * Inicializa los elementos de la vista, configura el juego y maneja las interacciones de navegación.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sopa)

        gridLayout = findViewById(R.id.grid_sopa_de_letras)
        buttonNext = findViewById(R.id.imageButtonNextActivity)
        textViewClues = findViewById(R.id.textViewClues)

        // Inicialmente ocultamos el botón
        buttonNext.visibility = ImageButton.INVISIBLE

        wordSearchLetters = generateRandomWordSearch()
        setupWordSearchGrid()
        updateCluesDisplay()
        // Encuentra el ImageButton de "Volver"
        val buttonBack = findViewById<ImageButton>(R.id.imageButtonBackToWelcome)

        buttonBack.setOnClickListener {
            // Crear un Intent para iniciar la actividad de bienvenida
            val intent = Intent(this, ActividadBienvenidaSopa::class.java)
            startActivity(intent)
            finish() // Opcional, si quieres cerrar la actividad actual
        }
        buttonNext.setOnClickListener {
            val intent = Intent(this, Mapa::class.java)
            intent.putExtra("ACTUALIZAR_PUNTOS", true)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()

        }

        buttonNext.isEnabled = false

        setupTouchListener()
    }
    /**
     * Configura el listener para las interacciones táctiles en la cuadrícula.
     * Detecta los movimientos del dedo y maneja el inicio, el movimiento y el fin de la selección de celdas.
     */
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
    /**
     * Maneja el inicio de un toque sobre la cuadrícula.
     * Selecciona la celda en la que el usuario comienza a tocar.
     */
    private fun handleTouchStart(event: MotionEvent) {
        val (row, col) = getTouchedCell(event) ?: return
        resetSelection()
        selectCell(row, col)
    }
    /**
     * Maneja el movimiento del toque sobre la cuadrícula.
     * Selecciona nuevas celdas a medida que el dedo se mueve por la pantalla.
     */
    private fun handleTouchMove(event: MotionEvent) {
        val (row, col) = getTouchedCell(event) ?: return
        if (row != lastSelectedRow || col != lastSelectedCol) {
            selectCell(row, col)
        }
    }
    /**
     * Maneja el final del toque.
     * Verifica si la palabra seleccionada es correcta y la resetea si no lo es.
     */
    private fun handleTouchEnd() {
        checkWord()
        resetSelection() // Solo se resetea después de comprobar si la palabra es correcta o no
    }
    /**
     * Obtiene la celda que fue tocada en la cuadrícula basada en las coordenadas del evento táctil.
     *
     * @param event El evento táctil que contiene la posición.
     * @return Un par de enteros que representa las coordenadas de la celda tocada.
     */
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
    /**
     * Marca la celda seleccionada y agrega la letra a la palabra temporal seleccionada.
     */
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
     /**
     * Resetea la selección de las celdas, restaurando su color y limpiando la palabra temporal.
     */
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
    /**
     * Verifica si la palabra seleccionada es correcta comparándola con la lista de palabras.
     *
     * @return true si la palabra seleccionada es correcta, false en caso contrario.
     */
    private fun isCorrectWord(): Boolean {
        return selectedWord.toString() in words
    }
    /**
     * Comprueba si la palabra seleccionada es correcta.
     * Si es correcta, la agrega a las palabras encontradas y actualiza las celdas en verde.
     */
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
                if (!cellWordsMap.containsKey(Pair(row, col))) {
                    cellWordsMap[Pair(row, col)] = mutableListOf()
                }
                cellWordsMap[Pair(row, col)]!!.add(currentWord)

                // Marcar como verde
                textView.setBackgroundColor(Color.GREEN)
            }

            if (foundWords.size == words.size) {
                buttonNext.isEnabled = true
                buttonNext.visibility = ImageButton.VISIBLE
            }
        } else {
            resetSelection()
        }
    }
    /**
     * Actualiza las pistas de palabras encontradas en la interfaz de usuario.
     */
    private fun updateCluesDisplay() {
        val cluesText = words.joinToString("\n") { word ->
            if (word in foundWords) "$word ✓" else word
        }
        textViewClues.text = cluesText
    }
    /**
     * Muestra un mensaje de finalización cuando el usuario ha encontrado todas las palabras.
     */
    private fun showCompletionMessage() {
        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle(getString(R.string.felicidades))
            .setMessage(getString(R.string.has_encontrado_todas_las_palabras))
            .setPositiveButton("OK", null)
            .create()
        dialog.show()
    }
    /**
     * Coloca una palabra en la cuadrícula de manera aleatoria.
     *
     * @param grid La cuadrícula donde se colocará la palabra.
     * @param word La palabra que se quiere colocar.
     * @param startRow Fila de inicio para colocar la palabra.
     * @param startCol Columna de inicio para colocar la palabra.
     * @param horizontal Determina si la palabra se coloca horizontal o verticalmente.
     * @return true si la palabra fue colocada correctamente, false en caso contrario.
     */
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
      /**
     * Genera una sopa de letras aleatoria llenando una cuadrícula con palabras y letras aleatorias.
     *
     * @return La cuadrícula de letras generada.
     */
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
    /**
 * Configura la cuadrícula de la sopa de letras, llenándola con las letras de `wordSearchLetters`.
 * Crea un `TextView` para cada celda de la cuadrícula y lo agrega a la vista `gridLayout`.
 * Establece los parámetros de diseño, tamaño, márgenes y colores para cada celda.
 */
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
    /**
 * Cambia el color de fondo de todas las celdas seleccionadas a un color uniforme.
 * Este método puede usarse para destacar las celdas seleccionadas con un color específico,
 * por ejemplo, para indicar que el usuario ha tocado o seleccionado esas celdas.
 *
 * @param color El color que se aplicará al fondo de las celdas seleccionadas.
 */
    private fun changeSelectedCellsColor(color: Int) {
        // Cambiar el color de fondo de las celdas seleccionadas a un color uniforme
        for ((row, col) in selectedCells) {
            val linearIndex = row * gridSize + col
            val textView = gridLayout.getChildAt(linearIndex) as TextView
            textView.setBackgroundColor(color)
        }
    }
}
