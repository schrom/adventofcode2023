package schrom.aoc2023

import java.io.File

class Gear {

    private val partsAttached = mutableListOf<EnginePart>()

    fun addPart(enginePart: EnginePart) {
        partsAttached.add(enginePart)
    }

    fun hasTwoPartsAttached() = partsAttached.size == 2

    fun gearRatio() = partsAttached[0].getValue() * partsAttached[1].getValue()
}

class EnginePart2 : EnginePart() {
    fun findGearNeighbour(schematic: Schematic, gearmap: Map<Pair<Int, Int>, Gear>) {
        val gears = mutableSetOf<Pair<Int, Int>>()
        digits.forEach {
            for (col in it.second - 1..it.second + 1) {
                for (row in it.third - 1..it.third + 1) {
                    val c = schematic[row, col]
                    if (c == '*') {
                        gears.add(Pair(row, col))
                    }
                }
            }
        }
        gears.forEach { gearmap[it]!!.addPart(this) }
    }
}

fun main() {
    val data = File("data/day3.txt").readLines()

    val schematic = Schematic(data)
    val parts = mutableListOf<EnginePart2>()
    val gearmap = mutableMapOf<Pair<Int, Int>, Gear>()

    data.forEachIndexed { row, s ->
        var part: EnginePart2? = null
        s.forEachIndexed { col, c ->
            if (c.isDigit()) {
                if (part == null) {
                    part = EnginePart2()
                }
                part!!.addDigit(c, col, row)
            }
            if (part != null && (!c.isDigit() || col == s.lastIndex)) {
                parts.add(part!!)
                part = null
            }
            if (c == '*') {
                gearmap[Pair(row, col)] = Gear()
            }
        }
    }

    parts.forEach {
        it.findGearNeighbour(schematic, gearmap)
    }

    val sum = gearmap
        .values
        .filter {
            it.hasTwoPartsAttached()
        }
        .sumOf {
            it.gearRatio()
        }

    println(sum)
}

