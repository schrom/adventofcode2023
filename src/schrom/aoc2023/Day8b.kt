package schrom.aoc2023

import java.io.File
import java.util.*

// thank you https://www.baeldung.com/kotlin/lcm
class LeastCommonMultiple {
    private fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
        var result = numbers[0]
        for (i in 1 until numbers.size) {
            result = findLCM(result, numbers[i])
        }
        return result
    }
}

fun main() {
    val data = File("data/day8.txt").readLines()

    val nodes = data.subList(2, data.size)
        .associate {
            val (id, left, right) = """(...) = \((...), (...)\)""".toRegex().find(it)!!.destructured
            Pair(id, Node(id, left, right))
        }

    val endingWithA = nodes.filter { it.key.endsWith("A") }.values
    val allSteps = endingWithA.map {
        val directions: Queue<Char> = data[0].asSequence().toCollection(LinkedList())
        var current = it
        var steps = 0L
        while (!current.id.endsWith("Z")) {
            val direction = directions.remove()
            directions.add(direction)
            current = nodes[current.getNext(direction)]!!
            steps++
        }
        steps
    }

    println(LeastCommonMultiple().findLCMOfListOfNumbers(allSteps))
}

