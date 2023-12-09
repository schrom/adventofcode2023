package schrom.aoc2023

import java.io.File

fun main() {
    val data = File("data/day9.txt").readLines()

    val oasisReadings = data
        .map {
            it.split(" ").map { n -> n.toInt() }.toMutableList()
        }.map {
            mutableListOf(it)
        }

    oasisReadings.forEach {
        while (it.last().any { number -> number != 0 }) {
            val listForDiffing = it.last()
            it.add(
                (1..listForDiffing.lastIndex).map { i ->
                    (listForDiffing[i] - listForDiffing[i-1])
                }.toMutableList()
            )
        }

        (it.lastIndex downTo 0).forEach{ index ->
            if(index == it.lastIndex){
                it[index].add(0)
            }else{
                it[index].add(it[index].last()+it[index+1].last())
            }
        }
    }

    val result = oasisReadings.sumOf {
        it.first().last()
    }

    println(result)
}

