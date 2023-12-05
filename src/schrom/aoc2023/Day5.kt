package schrom.aoc2023

import java.io.File

class Almanac {
    val conversions = mutableListOf<ConverterMap>()

    fun map(start: Long) = map(start, conversions)

    private fun map(start: Long, conversion: List<ConverterMap>): Long {
        return if (conversion.isEmpty()) {
            start
        } else {
            map(conversion.first().map(start), conversion.subList(1, conversion.size))
        }
    }
}

class ConverterMap(val name: String) {
    val mappings = mutableListOf<ConverterMapEntry>()

    fun map(start: Long): Long {
        mappings.forEach {
            if (it.range().contains(start)) {
                return it.destination(start)
            }
        }
        return start
    }
}

class ConverterMapEntry(private val destination: Long, private val source: Long, private val range: Long) {
    fun range() = source..<source + range
    fun destination(start: Long) = destination + (start - source)
}

fun main() {
    val data = File("data/day5.txt").readLines()

    val seeds = data.first()
        .substringAfter("seeds: ")
        .split(" ")
        .map {
            it.toLong()
        }

    val almanac = Almanac()
    var convertMap: ConverterMap? = null
    val mapData = data.subList(2, data.size)
    mapData.forEach {
        if (it.endsWith("map:")) {
            convertMap = ConverterMap(it)
            almanac.conversions.add(convertMap!!)
        } else if (it.isNotEmpty()) {
            val mapping = it.split(" ")
            convertMap!!.mappings.add(
                ConverterMapEntry(mapping[0].toLong(), mapping[1].toLong(), mapping[2].toLong())
            )
        }
    }

    val min = seeds.minOf {
        almanac.map(it)
    }

    println(min)
}

