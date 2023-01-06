package minesweeper

enum class CellValues(val textContext: String) {
    AN_UNEXPLORED_CELL_VALUE("."),
    AN_EXPLORED_WITHOUT_MINES_AROUND_IT_VALUE("/"),
    AN_UNEXPLORED_MARKED_CELL_VALUE("*"),
    MINED_CELL_VALUE("X"),
}