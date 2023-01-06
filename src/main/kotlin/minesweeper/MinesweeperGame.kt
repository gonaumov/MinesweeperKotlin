package minesweeper

import kotlin.random.Random

class MinesweeperGame(private val columns: Int, private val rows: Int) {
    private val board = MutableList(rows) {
        MutableList(columns) {
            CellValues.NON_MARKED_CELL_VALUE.textContext
        }
    }

    private val minesCoordinates = mutableListOf<Pair<Int, Int>>()

    val minesAreMarkedProperly: Boolean
        get() {
            val markedMinesCount = minesCoordinates.filter {
                val (first, second) = it
                board[first][second] == CellValues.MARKED_CELL_VALUE.textContext
            }.size
            return markedMinesCount == board.sumOf { strings ->
                strings.filter {
                    it == CellValues.MARKED_CELL_VALUE.textContext
                }.size
            } && markedMinesCount == minesCoordinates.size
        }

    fun markCell(inputX: Int, inputY: Int) {
        val x = inputX.dec()
        val y = inputY.dec()
        if (!listOf(
                CellValues.MARKED_CELL_VALUE.textContext,
                CellValues.NON_MARKED_CELL_VALUE.textContext
            ).contains(board[x][y])
        ) {
            throw MarkCellException("There is a number here!")
        }
        board[x][y] = if (board[x][y] == CellValues.MARKED_CELL_VALUE.textContext)
            CellValues.NON_MARKED_CELL_VALUE.textContext else CellValues.MARKED_CELL_VALUE.textContext
    }

    fun setMines(minesNumber: Int) {
        var mines = minesNumber
        do {
            val x = Random.nextInt(0, columns)
            val y = Random.nextInt(0, rows)
            val coordinates = minesCoordinates.find {
                val (first, second) = it
                first == x && second == y
            }
            if (coordinates == null) {
                minesCoordinates.add(Pair(x, y))
                mines--
            }
        } while (mines != 0)
    }

    private fun getMine(x: Int, y: Int): CellValues {
        return minesCoordinates.find {
            val (first, second) = it
            first == x && second == y
        }.let {
            if (it == null) {
                CellValues.NON_MARKED_CELL_VALUE
            } else {
                CellValues.MINED_CELL_VALUE
            }
        }
    }

    fun markMines() {
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (getMine(i, j) == CellValues.MINED_CELL_VALUE) {
                    continue
                }
                val hasLeftCell = j - 1 >= 0
                val hasRightCell = j + 1 <= columns - 1
                val hasTopRow = i - 1 >= 0
                val hasBottomRow = i + 1 <= rows - 1
                listOf(
                    if (hasLeftCell) {
                        getMine(i, j - 1)
                    } else {
                        CellValues.NON_MARKED_CELL_VALUE
                    },
                    if (hasRightCell) {
                        getMine(i, j + 1)
                    } else {
                        CellValues.NON_MARKED_CELL_VALUE
                    },
                    if (hasTopRow) {
                        getMine(i - 1, j)
                    } else {
                        CellValues.NON_MARKED_CELL_VALUE
                    },
                    if (hasBottomRow) {
                        getMine(i + 1, j)
                    } else {
                        CellValues.NON_MARKED_CELL_VALUE
                    },
                    if (hasTopRow && hasLeftCell) {
                        getMine(i - 1, j - 1)
                    } else {
                        CellValues.NON_MARKED_CELL_VALUE
                    },
                    if (hasBottomRow && hasLeftCell) {
                        getMine(i + 1, j - 1)
                    } else {
                        CellValues.NON_MARKED_CELL_VALUE
                    },
                    if (hasTopRow && hasRightCell) {
                        getMine(i - 1, j + 1)
                    } else {
                        CellValues.NON_MARKED_CELL_VALUE
                    },
                    if (hasBottomRow && hasRightCell) {
                        getMine(i + 1, j + 1)
                    } else {
                        CellValues.NON_MARKED_CELL_VALUE
                    }
                ).count {
                    it == CellValues.MINED_CELL_VALUE
                }.let {
                    if (it > 0) {
                        board[i][j] = it.toString()
                    }
                }
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