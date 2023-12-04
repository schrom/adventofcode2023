package schrom.aoc2023

import java.io.File
import kotlin.math.min

fun main() {
    val data = File("data/day4.txt").readLines()

    val myCards = mutableListOf<Pair<Card, Int>>()

    data.forEachIndexed { index, it ->
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
        myCards.add(index, Pair(card, 1))
    }

    var sum = myCards.size

    myCards.forEachIndexed { index, cards ->
        val matchingNumbers = cards.first.getMatchingNumbers()
        for (i in index + 1..min(index + matchingNumbers, myCards.lastIndex)) {
            myCards[i] = Pair(myCards[i].first, myCards[i].second + cards.second)
            sum += cards.second
        }
    }

    println(sum)
}

