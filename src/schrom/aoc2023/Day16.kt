package schrom.aoc2023

import java.io.File

val visited = mutableSetOf<Triple<Int, Int, BeamDirection>>()

enum class BeamDirection {
    U, D, L, R
}

class Beam(
    private var row: Int,
    private var col: Int,
    private var direction: BeamDirection
) {
    fun moveIn(grid: Grid) {
        if(!visited.add(Triple(row, col, direction))){
            return
        }

        grid.energize(row, col)

        when (grid[row, col]) {

            GridTile.EMPTY -> moveToNextPosition(grid)

            GridTile.MIRROR_1 -> {
                direction = when (direction) {
                    BeamDirection.U -> BeamDirection.R
                    BeamDirection.D -> BeamDirection.L
                    BeamDirection.L -> BeamDirection.D
                    BeamDirection.R -> BeamDirection.U
                }
                moveToNextPosition(grid)
            }

            GridTile.MIRROR_2 -> {
                direction = when (direction) {
                    BeamDirection.U -> BeamDirection.L
                    BeamDirection.D -> BeamDirection.R
                    BeamDirection.L -> BeamDirection.U
                    BeamDirection.R -> BeamDirection.D
                }
                moveToNextPosition(grid)
            }

            GridTile.SPLIT_1 -> when (direction) {
                BeamDirection.U, BeamDirection.D -> {
                    if (grid.hasCoordinates(row, col - 1)) {
                        Beam(row, col - 1, BeamDirection.L).moveIn(grid)

                    }
                    if (grid.hasCoordinates(row, col + 1)) {
                        Beam(row, col + 1, BeamDirection.R).moveIn(grid)
                    }
                }

                else -> moveToNextPosition(grid)
            }

            GridTile.SPLIT_2 -> when (direction) {
                BeamDirection.L, BeamDirection.R -> {
                    if (grid.hasCoordinates(row - 1, col)) {
                        Beam(row - 1, col, BeamDirection.U).moveIn(grid)
                    }
                    if (grid.hasCoordinates(row + 1, col)) {
                        Beam(row + 1, col, BeamDirection.D).moveIn(grid)
                    }
                }

                else -> moveToNextPosition(grid)
            }
        }
    }

    private fun moveToNextPosition(grid: Grid) {
        val (newRow, newCol) = newCoordsForDirection()
        if (grid.hasCoordinates(newRow, newCol)) {
            row = newRow
            col = newCol
            moveIn(grid)
        }
    }

    private fun newCoordsForDirection(): Pair<Int, Int> {
        return when (direction) {
            BeamDirection.U -> Pair(row-1, col)
            BeamDirection.D -> Pair(row+1, col)
            BeamDirection.L -> Pair(row, col-1)
            BeamDirection.R -> Pair(row, col+1)
        }
    }
}

enum class GridTile(val c: Char) {
    MIRROR_1('/'), MIRROR_2('\\'), SPLIT_1('-'), SPLIT_2('|'), EMPTY('.')
}

class Grid(rows: Int, columns: Int) {
    private val rows = Array(rows) { Array(columns) { GridTile.EMPTY } }

    private var energized = Array(rows) { Array(columns) { 0 } }

    operator fun set(row: Int, col: Int, item: GridTile) {
        rows[row][col] = item
    }

    operator fun get(row: Int, col: Int): GridTile {
        return rows[row][col]
    }

    fun energize(row: Int, col: Int) {
        energized[row][col]++
    }

    fun hasCoordinates(row: Int, col: Int): Boolean = row in 0..rows.lastIndex && col in 0..rows[0].lastIndex

    fun countEnergized(): Int {
       return energized.sumOf {
            it.map { f ->
                if(f>0){
                    1
                }else{
                    0
                }
            }.sum()
        }
    }

    fun resetEnergized() {
        energized = Array(rows.size) { Array(rows.first().size) { 0 } }
    }
}

// may need more than default stack size - "java -Xss1G" did fine for me

fun main() {
    val data = File("data/day16.txt").readLines()

    val grid = Grid(data.size, data.first().length)
    data.forEachIndexed { row, r ->
        r.forEachIndexed { col, c ->
            grid[row, col] = when (c) {
                '.' -> GridTile.EMPTY
                '-' -> GridTile.SPLIT_1
                '|' -> GridTile.SPLIT_2
                '/' -> GridTile.MIRROR_1
                '\\' -> GridTile.MIRROR_2
                else -> throw IllegalArgumentException()
            }
        }
    }

    Beam(0, 0, BeamDirection.R).moveIn(grid)

    println(grid.countEnergized())
}
