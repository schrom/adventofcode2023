package schrom.aoc2023

import java.io.File

class Race(private val time: Long, private val distance: Long) {

    fun getWinningDistances(): List<Pair<Long, Long>> {
        return (0..time)
            .map {
                Pair(it, (time - it) * it)
            }
            .filter {
                it.second > distance
            }
    }

}

fun main() {
    val data = File("data/day6.txt").readLines()

    val times = data[0].substringAfter(":").trim().split(""" +""".toRegex()).map { it.toLong() }
    val distances = data[1].substringAfter(":").trim().split(""" +""".toRegex()).map { it.toLong() }

    val result = times
        .mapIndexed { index, it ->
            Race(it, distances[index])
        }
        .map {
            it.getWinningDistances().size
        }
        .reduce { acc, it ->
            acc * it
        }

    println(result)
}

