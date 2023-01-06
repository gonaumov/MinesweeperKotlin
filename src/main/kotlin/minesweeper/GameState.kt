package minesweeper

enum class GameState(val message: String){
    IN_PROGRESS("Set/unset mine marks or claim a cell as free:"),
    STEPPED_ON_A_MINE("You stepped on a mine and failed!"),
    WIN("Congratulations! You found all the mines!")
}