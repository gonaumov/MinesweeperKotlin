package minesweeper

fun main(args: Array<String>) {
    println("How many mines do you want on the field?")
    val minesNumber = readln().toInt()
    val minesweeperGame = MinesweeperGame(9, 9)
    minesweeperGame.setMines(minesNumber)
    minesweeperGame.markMines()
    println(minesweeperGame.getBoard())
    do {
        println("Set/delete mines marks (x and y coordinates):")
        val (x, y) = readln().trim().split(" ").map {
            it.toInt()
        }
        try {
            minesweeperGame.markCell(y, x)
            println(minesweeperGame.getBoard())
        } catch (ex: MarkCellException) {
            println(ex.message)
        }
    } while (!minesweeperGame.minesAreMarkedProperly)
    println("Congratulations! You found all the mines!")
}