package schrom.aoc2023

import java.io.File

class GridOfTiles2(rows: Int, cols: Int) : GridOfTiles(rows, cols) {
    fun markRightHand(posDir: PositionAndDirection) {
        when (posDir.heading) {
            Heading.S -> {
                for (col in posDir.col - 1 downTo 0) {
                    if (this[posDir.row, col] == 'x') {
                        break
                    }
                    this[posDir.row, col] = 'I'
                }
            }

            Heading.N -> {
                for (col in posDir.col + 1..grid.lastIndex) {
                    if (this[posDir.row, col] == 'x') {
                        break
                    }
                    this[posDir.row, col] = 'I'
                }
            }

            Heading.W -> {
                for (row in posDir.row - 1 downTo 0) {
                    if (this[row, posDir.col] == 'x') {
                        break
                    }
                    this[row, posDir.col] = 'I'
                }
            }

            Heading.E -> {
                for (row in posDir.row + 1..grid[0].lastIndex) {
                    if (this[row, posDir.col] == 'x') {
                        break
                    }
                    this[row, posDir.col] = 'I'
                }
            }
        }
    }
}

fun main() {
    val data = File("data/day10.txt").readLines()

    val grid = GridOfTiles(data.size, data.first().length)
    data.forEachIndexed { row, r ->
        r.forEachIndexed { col, c ->
            grid[row, col] = c
        }
    }

    grid[grid.start!!.first, grid.start!!.second] = '7'

    var myPos = PositionAndDirection(grid.start!!.first, grid.start!!.second, Heading.S)

    val gridOfLoopTiles = GridOfTiles2(data.size, data.first().length)
    do {
        gridOfLoopTiles[myPos.row, myPos.col] = 'x'
        myPos = grid.nextField(myPos)
    } while (!myPos.isAt(grid.start!!))

    myPos = PositionAndDirection(grid.start!!.first, grid.start!!.second, Heading.S)
    do {
        val oldRow = myPos.row
        val oldCol = myPos.col
        myPos = grid.nextField(myPos)
        gridOfLoopTiles.markRightHand(myPos)
        gridOfLoopTiles.markRightHand(PositionAndDirection(oldRow, oldCol, myPos.heading))
    } while (!myPos.isAt(grid.start!!))

    val inside = gridOfLoopTiles.grid.sumOf {
        it.count { c -> c == 'I' }
    }
    println(inside)
}

