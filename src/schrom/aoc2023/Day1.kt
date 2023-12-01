package schrom.aoc2023

import java.io.File

fun main() {
    val data = File("data/day1.txt").readLines()

    val sum = data.sumOf {
        var firstDigit: Char? = null
        var lastDigit: Char? = null
        it.forEach { c ->
            if (c.isDigit()) {
                lastDigit = c
                if (firstDigit == null) {
                    firstDigit = c
                }
            }
        }
        "$firstDigit$lastDigit".toInt()
    }

    println(sum)
}
