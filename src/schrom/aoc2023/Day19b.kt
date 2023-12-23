package schrom.aoc2023

import java.io.File

class Range {
    var min = 1
    var max = 4000
}

class PossiblePart {
    private val ranges = mutableMapOf(
        'x' to Range(),
        'm' to Range(),
        'a' to Range(),
        's' to Range()
    )

    fun reduceByCondition(c: String) {
        var negated = false
        if (c == "always") {
            return
        }
        val condition = if (c.startsWith("!")) {
            negated = true
            c.substring(1)
        } else {
            c
        }
        val variable = condition[0]
        val newMinMax = condition.substring(2).toInt()
        when (condition[1]) {
            '>' -> {
                if (negated) {
                    ranges[variable]!!.max = newMinMax
                } else {
                    ranges[variable]!!.min = newMinMax + 1
                }
            }

            '<' -> {
                if (negated) {
                    ranges[variable]!!.min = newMinMax
                } else {
                    ranges[variable]!!.max = newMinMax - 1
                }
            }
        }
    }

    fun getNrOfCombinations() =
        (ranges['x']!!.max - ranges['x']!!.min + 1L) *
                (ranges['m']!!.max - ranges['m']!!.min + 1L) *
                (ranges['a']!!.max - ranges['a']!!.min + 1L) *
                (ranges['s']!!.max - ranges['s']!!.min + 1L)
}

class Day19Node(val id: String) {
    val edges = mutableListOf<Day19Edge>()
}

class Day19Edge(val to: Day19Node) {
    val conditions = mutableListOf<String>()
}

private fun findAllPaths(current: Day19Node, path: List<Day19Edge>, results: MutableList<List<Day19Edge>>) {
    if (current.id == "R") {
        return
    }
    if (current.id == "A") {
        results.add(path)
        return
    }
    current.edges.forEach {
        findAllPaths(it.to, path + it, results)
    }
}

fun main() {
    val graph = mutableMapOf<String, Day19Node>()
    val data = File("data/day19.txt").readLines()
    data.forEach {
        if (it.isNotEmpty() && !it.startsWith("{")) {
            val id = it.substringBefore("{")
            val node = graph.getOrDefault(id, Day19Node(id))
            val allRules = it.substringAfter("{").substringBefore("}").split(",")
            val conditions = allRules.map { c ->
                if (c.contains(":")) {
                    c.substringBefore(":")
                } else {
                    "always"
                }
            }
            allRules.forEachIndexed { i, c ->
                val target = if (c.contains(":")) {
                    c.substringAfter(":")
                } else {
                    c
                }
                val targetNode = graph.getOrDefault(target, Day19Node(target))
                val edge = Day19Edge(targetNode)
                (0..<i).forEach { prevConditions -> edge.conditions.add(("!" + conditions[prevConditions])) }
                edge.conditions.add(conditions[i])
                node.edges.add(edge)
                graph.putIfAbsent(id, node)
                graph.putIfAbsent(target, targetNode)
            }
        }
    }
    val allPaths = mutableListOf<List<Day19Edge>>()
    findAllPaths(graph["in"]!!, listOf(), allPaths)

    val result = allPaths.sumOf { edges ->
        val possiblePart = PossiblePart()
        edges.forEach { edge ->
            edge.conditions.forEach { condition ->
                possiblePart.reduceByCondition(condition)
            }
        }
        possiblePart.getNrOfCombinations()
    }
    println(result)
}


