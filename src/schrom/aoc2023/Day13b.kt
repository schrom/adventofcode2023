package schrom.aoc2023

import java.io.File

class ValleyWithSmudges(data: List<String>) : Valley(data) {

    fun checkFromLeftSmudged(old: Int): Int {
        (columns.lastIndex downTo 1).forEach { i ->
            val (check, s) = checkSmudged(columns, 0, i, 0)
            if (check != old && s == 1) {
                return check
            }
        }
        return 0
    }

    fun checkFromRightSmudged(old: Int): Int {
        (0..<columns.lastIndex).forEach { i ->
            val (check, s) = checkSmudged(columns, i, columns.lastIndex, 0)
            if (check != old && s == 1) {
                return check
            }
        }
        return 0
    }

    fun checkFromTopSmudged(old: Int): Int {
        (rows.lastIndex downTo 1).forEach { i ->
            val (check, s) = checkSmudged(rows, 0, i, 0)
            if (check != old && s == 1) {
                return check
            }
        }
        return 0
    }

    fun checkFromBottomSmudged(old: Int): Int {
        (0..<rows.lastIndex).forEach { i ->
            val (check, s) = checkSmudged(rows, i, rows.lastIndex, 0)
            if (check != old && s == 1) {
                return check
            }
        }
        return 0
    }

    private fun checkSmudged(data: List<String>, start: Int, end: Int, smudgeCount: Int): Pair<Int, Int> {

        if (end <= start || smudgeCount > 1) {
            return Pair(0, smudgeCount)
        }

        if (data[start] == data[end]) {
            if (start + 1 == end) {
                return if (smudgeCount == 1) {
                    Pair(start + 1, smudgeCount)
                } else {
                    Pair(0, smudgeCount)
                }
            } else {
                val (c, s) = checkSmudged(data, start + 1, end - 1, smudgeCount)
                if (c > 0 && s == 1) {
                    return Pair(c, s)
                }
            }
        }

        if (isSimilar(data[start], data[end])) {
            if (start + 1 == end) {
                return Pair(start + 1, smudgeCount + 1)
            } else {
                val (c, s) = checkSmudged(data, start + 1, end - 1, smudgeCount + 1)
                if (c > 0 && s == 1) {
                    return Pair(c, s)
                }
            }
        }

        return Pair(0, smudgeCount)
    }

    private fun isSimilar(s1: String, s2: String): Boolean {
        return s1
            .mapIndexed { i, c ->
                if (c == s2[i]) {
                    0
                } else {
                    1
                }
            }.sum() == 1
    }
}

fun main() {

    val data = File("data/day13.txt").readLines()

    val valleys = mutableListOf<ValleyWithSmudges>()
    var dataPart = mutableListOf<String>()
    data.forEachIndexed { index, s ->
        if (s.isEmpty() || index == data.lastIndex) {
            valleys.add(ValleyWithSmudges(dataPart))
            dataPart = mutableListOf()
        } else {
            dataPart.add(s)
        }
    }

    val result = valleys.sumOf {
        val oldLeft = it.checkFromLeft()
        val oldRight = it.checkFromRight()
        val oldTop = it.checkFromTop()
        val oldBottom = it.checkFromBottom()

        val leftForSmudges = it.checkFromLeftSmudged(oldLeft)
        val rightForSmudges = it.checkFromRightSmudged(oldRight)
        val topForSmudges = it.checkFromTopSmudged(oldTop)
        val bottomForSmudges = it.checkFromBottomSmudged(oldBottom)
        val r = leftForSmudges +
                rightForSmudges +
                topForSmudges * 100 +
                bottomForSmudges * 100
        r
    }

    println(result)
}
