package minesweeper

data class Cell(
    val x: Int,
    val y: Int,
    var hasMine: Boolean = false,
    var minesAround: Int = 0,
    var status: Status = Status.UNEXPLORED
) {
    enum class Status(val glyph: Char) {
        UNEXPLORED('.'),
        UNEXPLORED_MARKED('*'),
        EXPLORED_WITHOUT_MINES_AROUND('/'),
        EXPLORED_WITH_MINES_AROUND('?'),
        MINE('X')
    }
}