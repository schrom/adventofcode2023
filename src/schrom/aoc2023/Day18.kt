package schrom.aoc2023

import java.io.File

enum class DigType { BORDER_TRENCH, FILL_TRENCH, GROUND }

class DigMap {
    var posX = 0
    var posY = 0

    private val fields: MutableMap<Int, MutableMap<Int, DigType>> = mutableMapOf()

    fun digAtPosition() {
        this[posX, posY] = DigType.BORDER_TRENCH
    }

    fun fillUpward() {
        var y = posY - 1
        while (this[posX, y] != DigType.BORDER_TRENCH) {
            this[posX, y] = DigType.FILL_TRENCH
            y--
        }
    }

    fun fillRightward() {
        var x = posX + 1
        while (this[x, posY] != DigType.BORDER_TRENCH) {
            this[x, posY] = DigType.FILL_TRENCH
            x++
        }
    }


    operator fun set(x: Int, y: Int, type: DigType) {
        val row = fields.getOrDefault(y, mutableMapOf())
        row[x] = type
        fields[y] = row
    }

    operator fun get(x: Int, y: Int): DigType {
        val row = fields.getOrDefault(y, mutableMapOf())
        val digType = row.getOrDefault(x, DigType.GROUND)
        return digType
    }

    fun getNumberOfTrenches() =
        fields.map { row ->
            row.value.filter { it.value == DigType.FILL_TRENCH || it.value == DigType.BORDER_TRENCH }.count()
        }.sum()

}

fun main() {
    val data = File("data/day18.txt").readLines()

    val digMap = DigMap()
    digMap.digAtPosition()

    data.forEach {
        val direction = it.split(" ")[0]
        val amount = it.split(" ")[1].toInt()

        repeat(amount) {
            when (direction) {
                "U" -> digMap.posY--
                "D" -> digMap.posY++
                "R" -> digMap.posX++
                "L" -> digMap.posX--
            }
            digMap.digAtPosition()
        }
    }

    digMap.posX = 0
    digMap.posY = 0

    data.forEach {
        val direction = it.split(" ")[0]
        val amount = it.split(" ")[1].toInt()
        repeat(amount) {
            when (direction) {
                "U" -> {
                    digMap.fillRightward()
                    digMap.posY--
                }

                "D" -> {
                    digMap.posY++
                }

                "R" -> {
                    digMap.posX++
                }

                "L" -> {
                    digMap.fillUpward()
                    digMap.posX--
                }
            }
        }
        when (direction) {
            "U" -> digMap.fillRightward()
            "L" -> digMap.fillUpward()
        }

    }

    println(digMap.getNumberOfTrenches())
}
