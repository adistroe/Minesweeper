package minesweeper

class Minesweeper {
    enum class Message(val text: String) {
        HOW_MANY_MINES("How many mines do you want on the field? "),
        SET_UNSET_OR_CLAIM("Set/unset mine marks or claim a cell as free: "),
        CONGRATULATIONS("Congratulations! You found all the mines!"),
        STEPPED_ON_MINE("You stepped on a mine and failed!")
    }

    enum class Command(val text: String) {
        MINE("mine"),
        FREE("free")
    }

    enum class GameStatus {
        IS_RUNNING, PLAYER_WON, PLAYER_LOST
    }

    /**
     *  Main game loop
     */
    fun runGame() {
        var isFirstCell = true
        var gameStatus = GameStatus.IS_RUNNING

        print(Message.HOW_MANY_MINES.text)
        val howManyMines = readln().toInt()
        val game = Board(totalMines = howManyMines)

        do {
            print(Message.SET_UNSET_OR_CLAIM.text)
            val (argOne, argTwo, argThree) = readln().split(" ")
/*
            We swap the 'x,y' coordinates and subtract '1' because physical board coordinates are 1-9,
            while our object list starts counting from 0 -> 8
*/
            val y = argOne.toInt() - 1
            val x = argTwo.toInt() - 1
            when (argThree) {
                Command.FREE.text -> {
                    //  first cell is always safe, no mine
                    if (isFirstCell) {
                        isFirstCell = false
                        game.addMines(x, y)
                        game.exploreCells(x, y)
                        //  player explored all safe cells
                        if (game.allCellsAreExplored()) {
                            gameStatus = GameStatus.PLAYER_WON
                        }
                    } else {
                        val thisCell = game.board[x][y]
                        //  player stepped on mine
                        if (thisCell.hasMine) {
                            gameStatus = GameStatus.PLAYER_LOST
                            game.board.forEach { row ->
                                row.forEach { cell ->
                                    if (cell.hasMine) cell.status = Cell.Status.MINE
                                }
                            }
                        } else {
                            game.exploreCells(x, y)
                            //  player explored all safe cells
                            if (game.allCellsAreExplored()) {
                                gameStatus = GameStatus.PLAYER_WON
                            }
                        }
                    }
                }

                Command.MINE.text -> {
                    val cell = game.board[x][y]
                    if (cell.status == Cell.Status.UNEXPLORED || cell.status == Cell.Status.UNEXPLORED_MARKED) {
                        cell.status = if (cell.status == Cell.Status.UNEXPLORED_MARKED) {
                            Cell.Status.UNEXPLORED
                        } else {
                            Cell.Status.UNEXPLORED_MARKED
                        }
                        //  player correctly marked all mines, without any extra marked cells
                        if (game.allMinesAreCorrectlyMarked()) {
                            gameStatus = GameStatus.PLAYER_WON
                        }
                    }
                }
            }
            game.draw()

        } while (gameStatus == GameStatus.IS_RUNNING)

        println(
            if (gameStatus == GameStatus.PLAYER_WON) {
                Message.CONGRATULATIONS.text
            } else {
                Message.STEPPED_ON_MINE.text
            }
        )
    }
}