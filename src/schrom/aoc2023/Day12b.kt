package schrom.aoc2023

import java.io.File


class Condition2(private var record: String, private var groups: List<Int>) {

    data class CacheKey(val record: String, val groups: List<Int>)

    private val cache = mutableMapOf<CacheKey, Long>()

    fun getMatches(): Long {
        return getCombinations(record, groups)
    }

    private fun getCombinations(record: String, groups: List<Int>): Long {

        if (cache.containsKey(CacheKey(record, groups))) {
            return cache[CacheKey(record, groups)]!!
        }

        if (record.isEmpty()) {
            return if (groups.isEmpty()) {
                1 // no more springs, no more groups, this is fine
            } else {
                0
            }
        }

        val result = when (record.first()) {
            '.' -> getCombinations(record.substring(1), groups)
            '?' -> getCombinations('.' + record.substring(1), groups) + getCombinations('#' + record.substring(1), groups)
            '#' -> {
                if (groups.isEmpty()) {
                    // no more groups left for spring
                    return 0
                }
                val damagedSprings = groups.first()
                if (record.length >= damagedSprings && record.substring(0, damagedSprings).all { it == '?' || it == '#' }) {
                    val remaining = record.substring(damagedSprings)
                    if (remaining.isEmpty()) {
                        return getCombinations("", groups.slice(1..groups.lastIndex))
                    }
                    return when (remaining.first()) {
                        '.' -> getCombinations(remaining.substring(1), groups.slice(1..groups.lastIndex))
                        '?' -> getCombinations('.' + remaining.substring(1), groups.slice(1..groups.lastIndex))
                        '#' -> 0
                        else -> throw IllegalArgumentException(remaining)
                    }
                } else {
                    // no more springs left for group
                    return 0
                }
            }
            else -> throw IllegalArgumentException(record)
        }
        cache[CacheKey(record, groups)] = result
        return result
    }
}

fun main() {
    val data = File("data/day12.txt").readLines()

    val result = data.sumOf { line ->
        val split = line.split(" ")
        val record = split[0]
        val groups = split[1].split(",").map { it.toInt() }
        val condition = Condition2(
            List(5) { _ -> record }.joinToString("?"),
            List(groups.size * 5) { i -> groups[i % groups.size] }
        )
        condition.getMatches()
    }

    println(result)
}

