package minesweeper

import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

data class Board(val width: Int = 9, val height: Int = 9, val totalMines: Int) {
    val board: List<List<Cell>> = List(width) { row -> List(height) { cell -> Cell(row, cell) } }
    private val widthRange = 0 until width
    private val heightRange = 0 until height

    enum class Draw(val element: String) {
        VERTICAL("|"),
        HORIZONTAL("—")
    }

    /**
     *  Draws the board after each player move
     */
    fun draw() {
        val horizontal = Draw.HORIZONTAL.element
        val vertical = Draw.VERTICAL.element
        val cellNumbers = (1..width).joinToString("")
        val topOrBottom = "$horizontal$vertical${horizontal.repeat(width)}$vertical"
        val topOfTheBoard = " $vertical$cellNumbers$vertical\n$topOrBottom"
        println("\n$topOfTheBoard")

        for ((rowIndex, row) in board.withIndex()) {
            print("${rowIndex + 1}$vertical")
            for (cell in row) {
                print(
                    if (cell.minesAround != 0 && cell.status == Cell.Status.EXPLORED_WITH_MINES_AROUND) {
                        cell.minesAround
                    } else {
                        cell.status.glyph
                    }
                )
            }
            println(vertical)
        }
        println(topOrBottom)
    }

    /**
     *  Get all valid neighbours of a cell (adjacent cells with 'x,y' coordinates within board 'width,height')
     */
    private fun getAdjacentCellsOf(x: Int, y: Int) = listOf(
        Pair(x, y - 1),
        Pair(x, y + 1),
        Pair(x - 1, y),
        Pair(x + 1, y),
        Pair(x - 1, y - 1),
        Pair(x - 1, y + 1),
        Pair(x + 1, y + 1),
        Pair(x + 1, y - 1)
    ).filter { pair -> pair.first in widthRange && pair.second in heightRange }

    /**
     *  If the cell has a mine, then increases the minesAround count for each of its adjacent cells.
     */
    private fun updateMineCountForAdjacentCellsOf(x: Int, y: Int) = getAdjacentCellsOf(x, y).forEach { pair ->
        board[pair.first][pair.second].minesAround++
    }

    /**
     *  Adds mines to board, AFTER the player made first move.
     *  This guarantees first move is always safe
     */
    fun addMines(safeX: Int, safeY: Int) {
        val random = Random
        var counter = 0
        while (counter < totalMines) {
            val x = random.nextInt(widthRange)
            val y = random.nextInt(heightRange)
            // don't place mine on first explored cell, because it is always safe
            if (x == safeX && y == safeY) {
                continue
            }
            val cell = board[x][y]
            if (!cell.hasMine) {
                cell.hasMine = true
                //  in case mine is randomly placed on cell that already has neighbor cell with a mine
                cell.minesAround = 0
                updateMineCountForAdjacentCellsOf(x, y)
                counter++
            }
        }
    }

    /**
     *  Checks for win condition: if player correctly marked ALL and ONLY the mines.
     */
    fun allMinesAreCorrectlyMarked(): Boolean {
        var correctlyMarkedMines = 0
        var totalMarkedMines = 0
        board.forEach { row ->
            correctlyMarkedMines += row.count { cell ->
                cell.status == Cell.Status.UNEXPLORED_MARKED && cell.hasMine
            }
            totalMarkedMines += row.count { cell ->
                cell.status == Cell.Status.UNEXPLORED_MARKED
            }
        }
        return correctlyMarkedMines == totalMines && totalMines == totalMarkedMines
    }

    /**
     *  Checks for win condition: player successfully explored all cells without stepping on mine
     */
    fun allCellsAreExplored(): Boolean {
        var unexploredCells = 0
        board.forEach { row ->
            unexploredCells += row.count { cell ->
                cell.status == Cell.Status.UNEXPLORED || cell.status == Cell.Status.UNEXPLORED_MARKED
            }
        }
        return unexploredCells == totalMines
    }

    /**
     *  Exploring nearby safe cells using the flood-fill BFS algorithm
     */
    fun exploreCells(fromX: Int, fromY: Int) {
        val queue: Queue<Pair<Int, Int>> = LinkedList()
        queue.add(Pair(fromX, fromY))

        while (queue.isNotEmpty()) {
            val queueItem = queue.remove()
            val x = queueItem.first
            val y = queueItem.second
            val thisCell = board[x][y]
            // discard these cells from queue
            if (thisCell.hasMine
                || thisCell.status == Cell.Status.EXPLORED_WITH_MINES_AROUND
                || thisCell.status == Cell.Status.EXPLORED_WITHOUT_MINES_AROUND
            ) {
                continue
            }

            if (thisCell.minesAround == 0) {
                thisCell.status = Cell.Status.EXPLORED_WITHOUT_MINES_AROUND
                // add valid neighbour cells to queue
                getAdjacentCellsOf(x, y).forEach { pair -> queue.add(pair) }
            } else {
                thisCell.status = Cell.Status.EXPLORED_WITH_MINES_AROUND
            }
        }
    }

    init {
        // initial board is drawn, there are no mines placed at this time, waiting for player's first move
        draw()
    }
}