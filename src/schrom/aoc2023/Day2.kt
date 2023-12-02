package schrom.aoc2023

import java.io.File

open class Game(
    val id: Int,
) {

    var red: Int = 0
    var green: Int = 0
    var blue: Int = 0

    var isValid = true

    private val maxCubes = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14,
    )

    fun checkValid() {
        if (red > maxCubes["red"]!! || green > maxCubes["green"]!! || blue > maxCubes["blue"]!!) {
            isValid = false
        }
    }

    fun parseRevealed(revealed: String) {
        red = """(\d+) red""".toRegex().find(revealed)?.groups?.get(1)?.value?.toInt() ?: 0
        green = """(\d+) green""".toRegex().find(revealed)?.groups?.get(1)?.value?.toInt() ?: 0
        blue = """(\d+) blue""".toRegex().find(revealed)?.groups?.get(1)?.value?.toInt() ?: 0
    }

}

fun main() {
    val data = File("data/day2.txt").readLines()

    var sum = 0
    data.forEach {
        val id = it.substringBefore(":").substringAfter("Game ").toInt()
        val game = Game(id)
        it.substringAfter(": ").split("; ").forEach { revealed ->
            game.parseRevealed(revealed)
            game.checkValid()
        }
        if (game.isValid) {
            sum += game.id
        }
    }

    print(sum)
}

