package schrom.aoc2023

import java.io.File

fun mapFrom(c: Char): Item {
    return when (c) {
        '.' -> Item.EMPTY
        '#' -> Item.CUBE
        'O' -> Item.ROUND
        else -> throw IllegalArgumentException(c.toString())
    }
}


enum class Item {
    ROUND, CUBE, EMPTY
}

data class Rock(var row: Int, var col: Int)

class Platform(rows: Int, columns: Int) {
    private val rows = Array(rows) { Array(columns) { Item.EMPTY } }

    val roundRocks = mutableSetOf<Rock>()

    operator fun set(row: Int, col: Int, item: Item) {
        rows[row][col] = item
    }

    operator fun get(row: Int, col: Int): Item {
        return rows[row][col]
    }

    private fun hasCoordinates(row: Int, col: Int): Boolean = row in 0..rows.lastIndex && col in 0..rows[0].lastIndex

    fun getWeight(): Long {
        return rows.mapIndexed { index, r ->
            r.count { it == Item.ROUND } * (rows.lastIndex - index + 1L)
        }.sum()
    }

    fun moveNorth() {
        roundRocks.sortedBy { it.row }.forEach {
            var moveTo = it.row
            while (hasCoordinates(moveTo - 1, it.col) && this[moveTo - 1, it.col] == Item.EMPTY) {
                moveTo--
            }
            this[it.row, it.col] = Item.EMPTY
            this[moveTo, it.col] = Item.ROUND
            it.row = moveTo
        }
    }

    fun moveSouth() {
        roundRocks.sortedByDescending { it.row }.forEach {
            var moveTo = it.row
            while (hasCoordinates(moveTo + 1, it.col) && this[moveTo + 1, it.col] == Item.EMPTY) {
                moveTo++
            }
            this[it.row, it.col] = Item.EMPTY
            this[moveTo, it.col] = Item.ROUND
            it.row = moveTo
        }
    }

    fun moveEast() {
        roundRocks.sortedByDescending { it.col }.forEach {
            var moveTo = it.col
            while (hasCoordinates(it.row, moveTo + 1) && this[it.row, moveTo + 1] == Item.EMPTY) {
                moveTo++
            }
            this[it.row, it.col] = Item.EMPTY
            this[it.row, moveTo] = Item.ROUND
            it.col = moveTo
        }
    }

    fun moveWest() {
        roundRocks.sortedBy { it.col }.forEach {
            var moveTo = it.col
            while (hasCoordinates(it.row, moveTo - 1) && this[it.row, moveTo - 1] == Item.EMPTY) {
                moveTo--
            }
            this[it.row, it.col] = Item.EMPTY
            this[it.row, moveTo] = Item.ROUND
            it.col = moveTo
        }
    }

    fun getHashForRocks() = roundRocks.sumOf { (it.row + 1023*it.col).toLong() }
}

fun main() {

    val data = File("data/day14_test.txt").readLines()

    val platform = Platform(data.size, data.first().length)
    data.forEachIndexed { row, r ->
        r.forEachIndexed { col, c ->
            val item = mapFrom(c)
            platform[row, col] = item
            if (item == Item.ROUND) {
                platform.roundRocks.add(Rock(row, col))
            }
        }
    }

    platform.moveNorth()

    println(platform.getWeight())
}
