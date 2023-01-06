package minesweeper

fun main(args: Array<String>) {
    println("How many mines do you want on the field?")
    val minesNumber = readln().toInt()
    val minesweeperGame = MinesweeperGame(9, 9)
    minesweeperGame.setMines(minesNumber)
    minesweeperGame.markMines()
    println(minesweeperGame.getBoard())
}