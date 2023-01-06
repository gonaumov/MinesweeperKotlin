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

    fun markMines() {
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j] == MINED_CELL_VALUE) {
                    continue
                }
                val hasLeftCell = j - 1 >= 0
                val hasRightCell = j + 1 <= columns - 1
                val hasTopRow = i - 1 >= 0
                val hasBottomRow = i + 1 <= rows - 1
                listOf(
                    if (hasLeftCell) {
                        board[i][j - 1]
                    } else {
                        EMPTY_CELL_VALUE
                    },
                    if (hasRightCell) {
                        board[i][j + 1]
                    } else {
                        EMPTY_CELL_VALUE
                    },
                    if (hasTopRow) {
                        board[i - 1][j]
                    } else {
                        EMPTY_CELL_VALUE
                    },
                    if (hasBottomRow) {
                        board[i + 1][j]
                    } else {
                        EMPTY_CELL_VALUE
                    },
                    if (hasTopRow && hasLeftCell) {
                        board[i - 1][j - 1]
                    } else {
                        EMPTY_CELL_VALUE
                    },
                    if (hasBottomRow && hasLeftCell) {
                        board[i + 1][j - 1]
                    } else {
                        EMPTY_CELL_VALUE
                    },
                    if (hasTopRow && hasRightCell) {
                        board[i - 1][j + 1]
                    } else {
                        EMPTY_CELL_VALUE
                    },
                    if (hasBottomRow && hasRightCell) {
                        board[i + 1][j + 1]
                    } else {
                        EMPTY_CELL_VALUE
                    }
                ).count {
                    it == MINED_CELL_VALUE
                }.let {
                    if (it > 0) {
                        board[i][j] = it.toString()
                    }
                }
            }
        }
    }

    fun getBoard(): String {
        return board.fold("") { acc: String, strings: MutableList<String> ->
            acc + strings.joinToString("") + "\n"
        }
    }
}