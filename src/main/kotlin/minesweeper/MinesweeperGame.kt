package minesweeper

class MinesweeperGame(private val columns: Int, rows: Int) {
    private val board = MutableList(rows) {
            _: Int ->
        MutableList(columns) {
            getFieldContent()
        }
    }

    private fun getFieldContent(): String {
        return listOf(".", "X").random()
    }

    fun getBoard(): String {
        return board.fold("") {
                acc: String, strings: MutableList<String> ->
            acc + strings.joinToString("") + "\n"
        }
    }
}