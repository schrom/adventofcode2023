package schrom.aoc2023

import java.io.File

class Game2(id: Int) : Game(id) {
    private var minRed = 0
    private var minGreen = 0
    private var minBlue = 0

    fun checkAtLeastNecessary(){
        minRed = Integer.max(minRed, red)
        minGreen = Integer.max(minGreen, green)
        minBlue = Integer.max(minBlue, blue)
    }

    fun getPower() = minRed * minGreen * minBlue
}

fun main() {
    val data = File("data/day2.txt").readLines()

    var sum = 0
    data.forEach {
        val id = it.substringBefore(":").substringAfter("Game ").toInt()
        val game = Game2(id)
        it.substringAfter(": ").split("; ").forEach { revealed ->
            game.parseRevealed(revealed)
            game.checkAtLeastNecessary()
        }
        sum += game.getPower()
    }

    print(sum)
}

