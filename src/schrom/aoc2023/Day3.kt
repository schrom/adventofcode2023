package schrom.aoc2023

import java.io.File
import kotlin.math.pow

class Schematic(data: List<String>) {

    private val rows: Array<CharArray>

    init {
        rows = Array(data.size) { CharArray(data.first().length) { '.' } }
        data.forEachIndexed { r, s ->
            rows[r] = s.toCharArray()
        }
    }

    operator fun get(row: Int, col: Int): Char {
        if (col < 0 || col > rows.first().lastIndex || row < 0 || row > rows.lastIndex) {
            return '.'
        }
        return rows[row][col]
    }
}

open class EnginePart {
    protected val digits: MutableList<Triple<Char, Int, Int>> = mutableListOf()

    fun addDigit(digit: Char, x: Int, y: Int) {
        digits.add(Triple(digit, x, y))
    }

    fun getValue(): Int {
        return digits
            .reversed()
            .mapIndexed { index, d ->
                d.first.digitToInt() * (10F.pow(index))
            }
            .sum()
            .toInt()
    }

    fun hasSymbolNeighbour(schematic: Schematic): Boolean {
        // could be optimized, checks unnecessary fields and multiple times
        digits.forEach {
            for (col in it.second - 1..it.second + 1) {
                for (row in it.third - 1..it.third + 1) {
                    val c = schematic[row, col]
                    if (c != '.' && !c.isDigit()) {
                        return true
                    }
                }
            }
        }
        return false
    }
}

fun main() {
    val data = File("data/day3.txt").readLines()

    val schematic = Schematic(data)
    val engineParts = mutableListOf<EnginePart>()

    data.forEachIndexed { row, s ->
        var enginePart: EnginePart? = null
        s.forEachIndexed { col, c ->
            if (c.isDigit()) {
                if (enginePart == null) {
                    enginePart = EnginePart()
                }
                enginePart!!.addDigit(c, col, row)
            }
            if (enginePart != null && (!c.isDigit() || col == s.lastIndex)) {
                engineParts.add(enginePart!!)
                enginePart = null
            }
        }
    }

    val sum = engineParts
        .filter {
            it.hasSymbolNeighbour(schematic)
        }
        .sumOf {
            it.getValue()
        }

    println(sum)
}

