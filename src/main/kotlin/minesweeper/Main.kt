package minesweeper

import java.lang.NumberFormatException

fun main(args: Array<String>) {
    println("How many mines do you want on the field?")
    val minesNumber = readln().toInt()
    val minesweeperGame = MinesweeperGame(9, 9, minesNumber)
    while (true) {
        println(minesweeperGame.getBoard())
        println(minesweeperGame.gameState.message)
        if (minesweeperGame.gameState != GameState.IN_PROGRESS) {
            break
        }
        val arguments = readln().trim().split(" ")
        try {
            if (arguments.size != 3) {
                throw MarkCellException("usage: <number> <number> <mode>")
            }
            val (x, y, mode) = arguments
            minesweeperGame.markCell(y.toInt(), x.toInt(), mode)
        } catch (ex: NumberFormatException) {
            println("Please enter only numbers for coordinates.")
        } catch (ex: MarkCellException) {
            println(ex.message)
        }
    }
}
