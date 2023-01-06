package minesweeper

import kotlin.random.Random

private const val EMPTY_CELL_VALUE = "."
private const val MINED_CELL_VALUE = "X"

class MinesweeperGame(private val columns: Int, private val rows: Int) {
    private val board = MutableList(rows) {
        MutableList(columns) {
            EMPTY_CELL_VALUE
        }
    }

    fun setMines(minesNumber: Int) {
        var mines = minesNumber
        do {
            val x = Random.nextInt(0, columns)
            val y = Random.nextInt(0, rows)
            if (board[x][y] == EMPTY_CELL_VALUE) {
                board[x][y] = MINED_CELL_VALUE
                mines--
            }
        } while (mines != 0)
    }

    fun getBoard(): String {
        return board.fold("") { acc: String, strings: MutableList<String> ->
            acc + strings.joinToString("") + "\n"
        }
    }
}