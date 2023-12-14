package schrom.aoc2023

import java.io.File

open class Valley(data: List<String>) {

    protected val rows: MutableList<String>
    protected val columns: MutableList<String>

    init {
        rows = data.toMutableList()
        columns = (0..rows.first().lastIndex).map { i ->
            rows.map { r -> r[i] }.joinToString("")
        }.toMutableList()
    }

    fun checkFromLeft() = check(columns, 0, columns.lastIndex, true)
    fun checkFromRight() = check(columns, 0, columns.lastIndex, false)
    fun checkFromTop() = check(rows, 0, rows.lastIndex, true)
    fun checkFromBottom() = check(rows, 0, rows.lastIndex, false)

    private fun check(data: List<String>, start: Int, end: Int, startAtStart: Boolean): Int {
        if (end <= start) {
            return 0
        }
        if (data[start] == data[end]) {
            if (start + 1 == end) {
                return start + 1
            } else {
                val check = check(data, start + 1, end - 1, startAtStart)
                if (check > 0) {
                    return check
                }
            }
        }
        return if (startAtStart) {
            check(data, start, end - 1, true)
        } else {
            check(data, start + 1, end, false)
        }
    }

}

fun main() {

    val data = File("data/day13.txt").readLines()

    val valleys = mutableListOf<Valley>()
    var dataPart = mutableListOf<String>()
    data.forEachIndexed { index, s ->
        if (s.isEmpty() || index == data.lastIndex) {
            valleys.add(Valley(dataPart))
            dataPart = mutableListOf()
        } else {
            dataPart.add(s)
        }
    }

    val result = valleys.sumOf {
        it.checkFromLeft() +
                it.checkFromRight() +
                it.checkFromTop() * 100 +
                it.checkFromBottom() * 100
    }

    println(result)
}
