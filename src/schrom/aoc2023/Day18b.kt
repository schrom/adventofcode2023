package schrom.aoc2023

import java.io.File
import kotlin.math.min

class DigMapPart2 {
    var posX = 0L
    var posY = 0L

    private val corners: MutableList<Pair<Long, Long>> = mutableListOf()

    fun cornerAtPosition() {
        corners.add(Pair(posX, posY))
    }

    fun moveXY(xOffset: Long, yOffset: Long) {
        val newCorners = corners.map {
            Pair(it.first + xOffset, it.second + yOffset)
        }
        corners.clear()
        corners.addAll(newCorners)
    }

    // https://en.wikipedia.org/wiki/Shoelace_formula
    fun shoelace(): Long {
        val a = (0..<corners.lastIndex).sumOf { i ->
            corners[i].first * corners[i + 1].second - corners[i].second * corners[i + 1].first
        }
        return a / 2
    }
}

val hex2Direction = mapOf('0' to "R", '1' to "D", '2' to "L", '3' to "U")

@OptIn(ExperimentalStdlibApi::class) // String.hexToInt() in Kotlin 1.9 is experimental
fun main() {
    val data = File("data/day18.txt").readLines()

    val digMap = DigMapPart2()
    digMap.cornerAtPosition()

    var minX = 0L
    var minY = 0L

    var borders = 0L
    data.forEach {
        val hex = it.substringAfter("(#").substringBefore(")")
        val direction = hex2Direction[hex[5]]
        val amount = hex.substring(0, hex.length - 1).hexToInt()
        when (direction) {
            "U" -> digMap.posY -= amount
            "D" -> digMap.posY += amount
            "R" -> digMap.posX += amount
            "L" -> digMap.posX -= amount
        }
        borders += amount
        digMap.cornerAtPosition()
        minX = min(minX, digMap.posX)
        minY = min(minY, digMap.posY)
    }

    // shift whole area in same quadrant
    digMap.moveXY(0 - minX, 0 - minY)

    println(digMap.shoelace() + borders / 2 + 1)
}
