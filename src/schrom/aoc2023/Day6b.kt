package schrom.aoc2023

import java.io.File

fun main() {
    val data = File("data/day6.txt").readLines()

    val time = data[0].substringAfter(":").replace(" ", "").toLong()
    val distance = data[1].substringAfter(":").replace(" ", "").toLong()

    val result = Race(time, distance).getWinningDistances().size

    println(result)
}

