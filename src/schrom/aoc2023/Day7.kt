package schrom.aoc2023

import java.io.File

enum class HandType {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD,
}

class CamelCard(private val hand: String, val bid: Int) {

    class HandComparator : Comparator<CamelCard> {

        private val cardOrder = "AKQJT98765432"
            .asSequence()
            .mapIndexed { index, c ->
                c to index
            }.toMap()

        override fun compare(c1: CamelCard, c2: CamelCard): Int {
            val type1 = c1.getType()
            val type2 = c2.getType()
            if (type1 != type2) {
                return type2.compareTo(type1)
            } else {
                c1.hand.forEachIndexed { index, card1 ->
                    val card2 = c2.hand[index]
                    if (card1 != card2) {
                        return cardOrder[card2]!!.compareTo(cardOrder[card1]!!)
                    }
                }
            }
            throw IllegalStateException("WTF? two identical hands?")
        }
    }

    private fun getType(): HandType {
        val cardCount = mutableMapOf<Char, Int>()
        hand.asSequence().forEach {
            cardCount.putIfAbsent(it, 0)
            cardCount[it] = cardCount[it]!! + 1
        }
        val uniqueCards = cardCount.values.sortedDescending()

        return when (uniqueCards.size) {

            1 -> HandType.FIVE_OF_A_KIND

            2 -> {
                if (uniqueCards[0] == 4 && uniqueCards[1] == 1) {
                    return HandType.FOUR_OF_A_KIND
                }
                if (uniqueCards[0] == 3 && uniqueCards[1] == 2) {
                    return HandType.FULL_HOUSE
                }
                throw IllegalStateException(uniqueCards.toString())
            }

            3 -> {
                if (uniqueCards[0] == 3 && uniqueCards[1] == 1 && uniqueCards[2] == 1) {
                    return HandType.THREE_OF_A_KIND
                }
                if (uniqueCards[0] == 2 && uniqueCards[1] == 2 && uniqueCards[2] == 1) {
                    return HandType.TWO_PAIR
                }
                throw IllegalStateException(uniqueCards.toString())
            }

            4 -> HandType.ONE_PAIR

            else -> HandType.HIGH_CARD
        }
    }
}

fun main() {
    val data = File("data/day7.txt").readLines()

    val result = data
        .map {
            CamelCard(it.substringBefore(" "), it.substringAfter(" ").toInt())
        }
        .sortedWith(CamelCard.HandComparator())
        .mapIndexed { index, it ->
            it.bid * (index + 1)
        }
        .sum()

    println(result)
}

