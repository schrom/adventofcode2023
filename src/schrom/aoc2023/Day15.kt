package schrom.aoc2023

import java.io.File

fun hash(s: String): Int {
    var hash = 0
    s.forEach { c ->
        hash += c.code
        hash *= 17
        hash %= 256
    }
    return hash
}

fun main() {

    val data = File("data/day15.txt").readLines()

    val result = data.first().split(",")
        .sumOf {
            hash(it)
        }
    println(result)
}
