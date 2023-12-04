package schrom.aoc2023

import java.io.File
import kotlin.math.pow

class Card {

    private val winningNumbers = mutableSetOf<Int>()
    private val haveNumbers = mutableSetOf<Int>()

    fun addWinningNumber(number: Int) = winningNumbers.add(number)

    fun addNumber(number: Int) = haveNumbers.add(number)

    fun getMatchingNumbers() = (winningNumbers intersect haveNumbers).size

    fun getPoints(): Int {
        val matches = getMatchingNumbers()
        return if (matches == 0) {
            0
        } else {
            2F.pow(matches - 1).toInt()
        }
    }
}

fun main() {
    val data = File("data/day4.txt").readLines()

    val sum = data.sumOf {
        val card = Card()
        it
            .substringAfter(':')
            .substringBefore('|')
            .trim()
            .split(" +".toRegex())
            .forEach { n -> card.addWinningNumber(n.toInt()) }
        it
            .substringAfter('|')
            .trim()
            .split(" +".toRegex())
            .forEach { n -> card.addNumber(n.toInt()) }

        card.getPoints()
    }

    println(sum)
}

