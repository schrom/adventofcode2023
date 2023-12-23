package schrom.aoc2023

import java.io.File

fun main() {
    val data = File("data/day20.txt").readLines()

    val modules = data.associate {
        val typeAndId = it.substringBefore(" -> ")
        val module = if (typeAndId == "broadcaster") {
            Broadcaster(typeAndId)
        } else if (typeAndId.startsWith("%")) {
            FlipFlop(typeAndId.substring(1))
        } else {
            Conjunction(typeAndId.substring(1))
        }
        module.id to module
    }.toMutableMap()

    data.forEach {
        val typeAndId = it.substringBefore(" -> ")
        val id = if (typeAndId == "broadcaster") {
            typeAndId
        } else {
            typeAndId.substring(1)
        }
        val source = modules[id]!!
        val destinations = it.substringAfter(" -> ").split(", ")
        destinations
            .map { d ->
                modules.putIfAbsent(d, NoopModule(d))
                modules[d]!!
            }
            .forEach { m ->
                if (m is Conjunction) {
                    m.allSources.add(source)
                }
                source.destinations.add(m)
            }
    }

    val lastLow = mutableMapOf<String, Int>()
    val cycleForModule = mutableMapOf<String, Int>()
    var buttonCount = 0
    repeat(20000) {// should be good enough to detect the cycle
        buttonCount++
        (modules["broadcaster"] as Broadcaster).pushButton()
        listOf("lk", "zv", "sp", "xt").forEach { // input nodes of dg, which is the input node of rx
            if (modules[it]!!.lowCount > 0) {
                modules[it]!!.lowCount = 0
                cycleForModule[it] = buttonCount - lastLow.getOrDefault(it, 0)
                lastLow[it] = buttonCount
            }
        }
    }
    val lcm = LeastCommonMultiple() // hello there, day 8
    val result = lcm.findLCMOfListOfNumbers(cycleForModule.values.map { it.toLong() }.toList())
    println(result)
}
