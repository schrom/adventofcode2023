package schrom.aoc2023

import java.io.File

val digits = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9",
)

fun main() {
    val data = File("data/day1.txt").readLines()

    val sum = data
        .map {
            var replaced = ""
            for (i in it.indices) {
                if (it[i].isDigit()) {
                    replaced += it[i]
                }
                digits.forEach { d ->
                    if (it.substring(i).startsWith(d.key)) {
                        replaced += d.value
                    }
                }
            }
            replaced
        }
        .sumOf {
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
