package schrom.aoc2023

import java.io.File


fun main() {

    val data = File("data/day14.txt").readLines()

    val platform = Platform(data.size, data.first().length)
    data.forEachIndexed { row, r ->
        r.forEachIndexed { col, c ->
            val item = mapFrom(c)
            platform[row, col] = item
            if (item == Item.ROUND) {
                platform.roundRocks.add(Rock(row, col))
            }
        }
    }

    val shakeMap = mutableMapOf<Long, Long>()
    val weightMap = mutableMapOf<Long, Long>()
    var hashForRocks = platform.getHashForRocks()
    repeat(1000000000) {
        if(shakeMap.contains(hashForRocks)){
            hashForRocks = shakeMap[hashForRocks]!!
        }else{
            platform.moveNorth()
            platform.moveWest()
            platform.moveSouth()
            platform.moveEast()
            val newHash = platform.getHashForRocks()
            shakeMap[hashForRocks] = newHash
            hashForRocks = newHash
            weightMap[newHash] = platform.getWeight()
        }
    }

    println(weightMap[hashForRocks])
}
