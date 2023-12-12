package schrom.aoc2023

import java.io.File

class Condition(private var record: String, private var groups: List<Int>) {
    fun getMatches(): List<String> {
        val combinations = getCombinations(record, emptyList())
        return combinations.filter {
            isMatch(it, groups)
        }
    }

    private fun isMatch(it: String, groups: List<Int>): Boolean {
        return groups == it
            .trim('.')
            .split("""\.+""".toRegex())
            .map { it.length }

    }

    private fun getCombinations(record: String, results: List<String>): List<String> {
        if (!record.contains('?')) {
            return results + record
        }
        if (record[0] == '?') {
            return results +
                    getCombinations('.' + record.substring(1), results) +
                    getCombinations('#' + record.substring(1), results)
        }
        return results +
                getCombinations(record.substring(1), results).map {
                    record[0] + it
                }
    }
}

fun main() {
    val data = File("data/day12.txt").readLines()

    val result = data.sumOf { line ->
        val split = line.split(" ")
        val record = split[0]
        val groups = split[1].split(",").map { it.toInt() }
        val condition = Condition(record, groups)
        val matches = condition.getMatches()
        matches.count()
    }

    println(result)
}

