package schrom.aoc2023

import java.io.File

class Boxes {
    private val box = List(256) {
        Pair(mutableMapOf<String, Int>(), mutableListOf<String>())
    }

    fun add(lens: String, focus: Int, nr: Int) {
        if (box[nr].first.contains(lens)) {
            box[nr].first[lens] = focus
        } else {
            box[nr].first[lens] = focus
            box[nr].second.add(lens)
        }
    }

    fun remove(lens: String, nr: Int) {
        box[nr].first.remove(lens)
        box[nr].second.remove(lens)
    }

    fun getFocusPower(): Int {
        return box.flatMapIndexed { index, b ->
            val boxFactor = if (b.second.isNotEmpty()) {
                (index + 1)
            } else {
                0
            }
            val sum = b.second.mapIndexed { i, s ->
                boxFactor * (i + 1) * b.first[s]!!
            }
            sum
        }.sum()
    }
}

fun main() {

    val data = File("data/day15.txt").readLines()

    val boxes = Boxes()
    data.first().split(",")
        .forEach {
            if (it.endsWith("-")) {
                val lens = it.substringBefore("-")
                boxes.remove(lens, hash(lens))
            } else {
                val split = it.split("=")
                val lens = split[0]
                val focus = split[1].toInt()
                boxes.add(lens, focus, hash(lens))
            }
        }
    println(boxes.getFocusPower())
}
