package minesweeper

import kotlin.random.Random

class MinesweeperGame(private val columns: Int, private val rows: Int, private val minesNumber: Int) {
    private var board: MutableList<MutableList<String>> = MutableList(rows) {
        MutableList(columns) {
            CellValues.AN_UNEXPLORED_CELL_VALUE.textContext
        }
    }

    private val minesCoordinates = mutableListOf<Pair<Int, Int>>()

    var gameState: GameState = GameState.IN_PROGRESS

    private val minesAreMarkedProperly: Boolean
        get() {
            val markedMinesCount = minesCoordinates.filter {
                val (first, second) = it
                board[first][second] == CellValues.AN_UNEXPLORED_MARKED_CELL_VALUE.textContext
            }.size
            return markedMinesCount == board.sumOf { strings ->
                strings.filter {
                    it == CellValues.AN_UNEXPLORED_MARKED_CELL_VALUE.textContext
                }.size
            } && markedMinesCount == minesCoordinates.size
        }

    private var minesAreVisible: Boolean = false

    fun markCell(inputX: Int, inputY: Int, mode: String) {
        val x = inputX.dec()
        val y = inputY.dec()
        if (minesCoordinates.size == 0) {
            setMines(minesNumber, Pair(x, y))
        }
        when (mode) {
            "free" -> markCellAsFree(x, y)
            "mine" -> markCellAsMined(x, y)
            else -> throw MarkCellException("Unknown mode")
        }.apply {
            onMarkCell()
        }
    }

    private fun markCellAsMined(x: Int, y: Int): Boolean {
        return when (board[x][y]) {
            CellValues.AN_UNEXPLORED_CELL_VALUE.textContext -> {
                board[x][y] = CellValues.AN_UNEXPLORED_MARKED_CELL_VALUE.textContext
                true
            }

            CellValues.AN_UNEXPLORED_MARKED_CELL_VALUE.textContext -> {
                board[x][y] = CellValues.AN_UNEXPLORED_CELL_VALUE.textContext
                true
            }

            else -> true
        }
    }

    private fun onMarkCell() {
        if (minesAreMarkedProperly) {
            gameState = GameState.WIN
        } else if (minesAreVisible) {
            gameState = GameState.STEPPED_ON_A_MINE
        }
    }

    private fun markCellAsFree(x: Int, y: Int): Boolean {
        if (isThereAMineAtTheseCoordinates(x, y)) {
            board[x][y] = CellValues.MINED_CELL_VALUE.textContext
        }
        return when (board[x][y]) {
            CellValues.MINED_CELL_VALUE.textContext -> {
                revealMines()
                minesAreVisible = true
                false
            }

            CellValues.AN_UNEXPLORED_CELL_VALUE.textContext -> {
                markCellAsExplored(x, y)
                true
            }

            else -> true
        }
    }

    private fun revealMines() {
        minesCoordinates.forEach {
            val (first, second) = it
            board[first][second] = CellValues.MINED_CELL_VALUE.textContext
        }
    }

    private fun markCellAsExplored(x: Int, y: Int) {
        if (areThereMinesAround(x, y)) {
            val cells = getCoordinatesOfTheSurroundingCells(x, y)
            if (!isThereAMineAtTheseCoordinates(x, y)) {
                cells.add(Pair(x, y))
            }
            cells.forEach {
                val (first, second) = it
                countAndNoteTheSurroundingMinesAtThisLocation(first, second)
            }
            if (board[x][y] == CellValues.AN_UNEXPLORED_CELL_VALUE.textContext || board[x][y] == CellValues.AN_UNEXPLORED_MARKED_CELL_VALUE.textContext) {
                board[x][y] = CellValues.AN_EXPLORED_WITHOUT_MINES_AROUND_IT_VALUE.textContext
            }
        } else if (!areThereMinesAround(x, y)) {
            board[x][y] = CellValues.AN_EXPLORED_WITHOUT_MINES_AROUND_IT_VALUE.textContext
            val coordinatesOfTheSurroundingCells = getCoordinatesOfTheSurroundingCells(x, y)
            val freeCells = coordinatesOfTheSurroundingCells.filter {
                val (first, second) = it
                (board[first][second] == CellValues.AN_UNEXPLORED_CELL_VALUE.textContext ||
                        board[first][second] == CellValues.AN_UNEXPLORED_MARKED_CELL_VALUE.textContext)
                        && !areThereMinesAround(first, second)
            }
            coordinatesOfTheSurroundingCells.filter {
                val (first, second) = it
                areThereMinesAround(first, second)
            }.let {
                it.forEach {
                    val (first, second) = it
                    countAndNoteTheSurroundingMinesAtThisLocation(first, second)
                }
            }
            freeCells.forEach {
                val (first, second) = it
                board[first][second] = CellValues.AN_EXPLORED_WITHOUT_MINES_AROUND_IT_VALUE.textContext
                markCellAsExplored(first, second)
            }
        }
    }

    private fun setMines(minesNumber: Int, avoidCoordinates: Pair<Int, Int>) {
        var mines = minesNumber
        do {
            val generatedCoordinates = Pair(
                Random.nextInt(0, columns),
                Random.nextInt(0, rows)
            )
            val coordinates = minesCoordinates.find {
                it == generatedCoordinates
            }

            if (coordinates == null && generatedCoordinates != avoidCoordinates) {
                minesCoordinates.add(generatedCoordinates)
                mines--
            }
        } while (mines != 0)
    }

    private fun getCoordinatesOfTheSurroundingCells(i: Int, j: Int): MutableList<Pair<Int, Int>> {
        val surroundingMinesCoordinates = mutableListOf<Pair<Int, Int>>()
        val hasLeftCell = j - 1 >= 0
        val hasRightCell = j + 1 <= columns - 1
        val hasTopRow = i - 1 >= 0
        val hasBottomRow = i + 1 <= rows - 1

        if (hasLeftCell) {
            surroundingMinesCoordinates.add(Pair(i, j - 1))
        }

        if (hasRightCell) {
            surroundingMinesCoordinates.add(Pair(i, j + 1))
        }

        if (hasTopRow) {
            surroundingMinesCoordinates.add(Pair(i - 1, j))
        }

        if (hasBottomRow) {
            surroundingMinesCoordinates.add(Pair(i + 1, j))
        }

        if (hasTopRow && hasLeftCell) {
            surroundingMinesCoordinates.add(Pair(i - 1, j - 1))
        }

        if (hasBottomRow && hasLeftCell) {
            surroundingMinesCoordinates.add(Pair(i + 1, j - 1))
        }

        if (hasTopRow && hasRightCell) {
            surroundingMinesCoordinates.add(Pair(i - 1, j + 1))
        }

        if (hasBottomRow && hasRightCell) {
            surroundingMinesCoordinates.add(Pair(i + 1, j + 1))
        }

        return surroundingMinesCoordinates
    }

    private fun areThereMinesAround(i: Int, j: Int): Boolean {
        return getCoordinatesOfTheSurroundingCells(i, j).count {
            val (first, second) = it
            val result = isThereAMineAtTheseCoordinates(first, second)
            result
        } > 0
    }

    private fun isThereAMineAtTheseCoordinates(x: Int, y: Int): Boolean {
        val coordinates = minesCoordinates.find {
            val (first, second) = it
            first == x && second == y
        }
        return coordinates != null
    }

    private fun countAndNoteTheSurroundingMinesAtThisLocation(x: Int, y: Int) {
        getCoordinatesOfTheSurroundingCells(x, y).count {
            val (first, second) = it
            isThereAMineAtTheseCoordinates(first, second)
        }.let {
            if (it > 0 && !isThereAMineAtTheseCoordinates(x, y)) {
                board[x][y] = it.toString()
            }
        }
    }

    fun getBoard(): String {
        val boardContent = board.foldIndexed("") { index: Int, acc: String, strings: MutableList<String> ->
            acc + index.inc().toString() + "|" + strings.joinToString("") + "|\n"
        }
        val header = board.foldIndexed(" |") { index: Int, acc: String, _: MutableList<String> ->
            acc + index.inc().toString()
        } + "│\n"
        val border = "—│" + "—".repeat(board.size) + "│\n"
        return header + border + boardContent + border
    }
}