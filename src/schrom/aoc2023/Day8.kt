package schrom.aoc2023

import java.io.File
import java.util.*

class Node(val id: String, private val left: String, private val right: String) {
    fun getNext(direction: Char): String {
        return if (direction == 'L') {
            left
        } else {
            right
        }
    }
}

fun main() {
    val data = File("data/day8.txt").readLines()

    val directions: Queue<Char> = data[0].asSequence().toCollection(LinkedList())

    val nodes = data.subList(2, data.size)
        .associate {
            val (id, left, right) = """(...) = \((...), (...)\)""".toRegex().find(it)!!.destructured
            Pair(id, Node(id, left, right))
        }

    var current = nodes["AAA"]
    var steps = 0
    while (current!!.id != "ZZZ") {
        val direction = directions.remove()
        directions.add(direction)
        current = nodes[current.getNext(direction)]
        steps++
    }
    println(steps)
}

