package schrom.aoc2023

import java.io.File

fun main() {
    val data = File("data/day11.txt").readLines()
    val universe = TheUniverse(data.first().length, data.size)
    data.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            universe.setMap(x, y, c)
        }
    }
    universe.calculateExpansion(1000000)

    val allDistances = universe.galaxyDistances().sum()

    println(allDistances)
}
