package schrom.aoc2023

import java.io.File
import kotlin.math.max
import kotlin.math.min

class TheUniverse(private var height: Int, private var width: Int) {

    private val image: Array<CharArray> = Array(width) { CharArray(height) { '.' } }
    private val stepsAfterExpansion: Array<LongArray> = Array(width) { LongArray(height) { 1L } }
    private val galaxyMap = mutableListOf<Pair<Int, Int>>()

    fun setMap(x: Int, y: Int, c: Char) {
        image[x][y] = c
        if (c == '#') {
            galaxyMap.add(Pair(x, y))
        }
    }

    fun calculateExpansion(hubbleConstant: Long) {
        (0..<width).forEach { x ->
            var emptyColumn = true
            (0..<height).forEach { y ->
                if (image[x][y] != '.') {
                    emptyColumn = false
                }
            }
            if (emptyColumn) {
                (0..<height).forEach { y ->
                    stepsAfterExpansion[x][y] *= hubbleConstant
                }
            }
        }
        (0..<height).forEach { y ->
            var emptyRow = true
            (0..<width).forEach { x ->
                if (image[x][y] != '.') {
                    emptyRow = false
                }
            }
            if (emptyRow) {
                (0..<width).forEach { x ->
                    stepsAfterExpansion[x][y] *= hubbleConstant
                }
            }
        }
    }

    fun galaxyDistances(): List<Long> {
        return (0..<galaxyMap.lastIndex).flatMap { i ->
            (i+1..galaxyMap.lastIndex).map { j ->
                calculateDistanceBetween(galaxyMap[i], galaxyMap[j])
            }
        }
    }

    private fun calculateDistanceBetween(from: Pair<Int, Int>, to: Pair<Int, Int>): Long {
        val xDist = (min(from.first, to.first)..<max(from.first, to.first)).sumOf {
            stepsAfterExpansion[it][from.second]
        }
        val yDist = (min(from.second, to.second)..<max(from.second, to.second)).sumOf {
            stepsAfterExpansion[from.first][it]
        }
        return xDist + yDist
    }

}

fun main() {
    val data = File("data/day11.txt").readLines()
    val universe = TheUniverse(data.first().length, data.size)
    data.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            universe.setMap(x, y, c)
        }
    }
    universe.calculateExpansion(2)

    val allDistances = universe.galaxyDistances().sum()

    println(allDistances)
}
