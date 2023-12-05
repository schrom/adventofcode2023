package schrom.aoc2023

import java.io.File


fun main() {
    val data = File("data/day5.txt").readLines()

    val seedRanges = data
        .first()
        .substringAfter("seeds: ")
        .split(" ")
        .chunked(2)
        .map {
            it[0].toLong()..<it[0].toLong() + it[1].toLong()
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

    // simple brute forcing ...
    val min = seedRanges
        .mapIndexed { index, it ->
            println("range $index")
            it.minOf { s ->
                almanac.map(s)
            }
        }
        .minOf { it }


    println(min)
}

