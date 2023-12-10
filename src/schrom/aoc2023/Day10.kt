package schrom.aoc2023

import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.ceil

data class PositionAndDirection(val row: Int, val col: Int, val heading: Heading){
    fun isAt(pos: Pair<Int, Int>) = row == pos.first && col == pos.second
}

open class GridOfTiles(rows: Int, cols: Int) {

    var start: Pair<Int, Int>? = null
    val grid: Array<Array<Char>>

    init {
        grid = Array(cols) { Array(rows) { '.' } }
    }

    operator fun set(row: Int, col: Int, c: Char) {
        grid[col][row] = c
        if (c == 'S') {
            start = Pair(row, col)
        }
    }

    operator fun get(row: Int, col: Int): Char {
        return grid[col][row]
    }

    fun nextField(posDir: PositionAndDirection): PositionAndDirection {
        when (this[posDir.row, posDir.col]) {
            '-' -> return if (posDir.heading == Heading.E) {
                PositionAndDirection(posDir.row, posDir.col + 1, Heading.E)
            } else {
                PositionAndDirection(posDir.row, posDir.col - 1, Heading.W)
            }

            '|' -> return if (posDir.heading == Heading.S) {
                PositionAndDirection(posDir.row + 1, posDir.col, Heading.S)
            } else {
                PositionAndDirection(posDir.row - 1, posDir.col, Heading.N)
            }

            'L' -> return if (posDir.heading == Heading.S) {
                PositionAndDirection(posDir.row, posDir.col + 1, Heading.E)
            } else {
                PositionAndDirection(posDir.row - 1, posDir.col, Heading.N)
            }

            'J' -> return if (posDir.heading == Heading.S) {
                PositionAndDirection(posDir.row, posDir.col - 1, Heading.W)
            } else {
                PositionAndDirection(posDir.row - 1, posDir.col, Heading.N)
            }

            '7' -> return if (posDir.heading == Heading.N) {
                PositionAndDirection(posDir.row, posDir.col - 1, Heading.W)
            } else {
                PositionAndDirection(posDir.row + 1, posDir.col, Heading.S)
            }

            'F' -> return if (posDir.heading == Heading.N) {
                PositionAndDirection(posDir.row, posDir.col + 1, Heading.E)
            } else {
                PositionAndDirection(posDir.row + 1, posDir.col, Heading.S)
            }
        }
        throw IllegalArgumentException(posDir.toString())
    }
}

enum class Heading {
    N, E, S, W
}

fun main() {
    val data = File("data/day10.txt").readLines()

    val grid = GridOfTiles(data.size, data.first().length)
    data.forEachIndexed { row, r ->
        r.forEachIndexed { col, c ->
            grid[row, col] = c
        }
    }
    // start symbol determined manually by looking at the input data :-)
    grid[grid.start!!.first, grid.start!!.second] = '7' //'F'

    var steps = 0
    // start heading determined manually by looking at the input data :-)
    var myPos = PositionAndDirection( grid.start!!.first, grid.start!!.second, Heading.S) //  Heading.N)

    do {
        myPos = grid.nextField(myPos)
        steps++
    } while (!myPos.isAt(grid.start!!))

    println(ceil((steps /2F)).toInt())
}

