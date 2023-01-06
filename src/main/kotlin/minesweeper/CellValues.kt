package minesweeper

enum class CellValues(val textContext: String) {
    NON_MARKED_CELL_VALUE("."),
    MARKED_CELL_VALUE("*"),
    MINED_CELL_VALUE("X"),
}