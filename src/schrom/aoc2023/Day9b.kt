package schrom.aoc2023

import java.io.File
import java.util.LinkedList

fun main() {
    val data = File("data/day9.txt").readLines()

    val oasisReadings = data
        .map {
            it.split(" ").map { n -> n.toInt() }.toCollection(LinkedList())
        }.map {
            mutableListOf(it)
        }

    oasisReadings.forEach {
        while (it.last().any { number -> number != 0 }) {
            val listForDiffing = it.last()
            it.add(
                (1..listForDiffing.lastIndex).map { i ->
                    (listForDiffing[i] - listForDiffing[i - 1])
                }.toCollection(LinkedList())
            )
        }

        (it.lastIndex downTo 0).forEach { index ->
            if (index == it.lastIndex) {
                it[index].addFirst(0)
            } else {
                it[index].addFirst(it[index].first() - it[index + 1].first())
            }
        }
    }

    val result = oasisReadings.sumOf {
        it.first().first()
    }

    println(result)
}

