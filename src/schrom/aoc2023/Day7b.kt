package schrom.aoc2023

import java.io.File

class CamelCard2( val hand: String, val bid: Int) {

    class HandComparator : Comparator<CamelCard2> {

        private val cardOrder = "AKQT98765432J"
            .asSequence()
            .mapIndexed { index, c ->
                c to index
            }.toMap()

        override fun compare(c1: CamelCard2, c2: CamelCard2): Int {
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

    fun getType(): HandType {
        val cardCount = mutableMapOf<Char, Int>()
        var jokers = 0
        hand.asSequence().forEach {
            if (it != 'J') {
                cardCount.putIfAbsent(it, 0)
                cardCount[it] = cardCount[it]!! + 1
            } else {
                jokers++
            }
        }
        val uniqueCards = cardCount.values.sortedDescending()

        return when (uniqueCards.size) {

            1 -> {
                HandType.FIVE_OF_A_KIND
            }

            2 -> {
                if (uniqueCards[0] == 4 && uniqueCards[1] == 1 ||
                    uniqueCards[0] == 3 && uniqueCards[1] == 1 && jokers == 1 ||
                    uniqueCards[0] == 2 && uniqueCards[1] == 1 && jokers == 2 ||
                    uniqueCards[0] == 1 && uniqueCards[1] == 1 && jokers == 3
                ) {
                    return HandType.FOUR_OF_A_KIND
                }
                if (uniqueCards[0] == 3 && uniqueCards[1] == 2 ||
                    uniqueCards[0] == 2 && uniqueCards[1] == 2 && jokers == 1
                ) {
                    return HandType.FULL_HOUSE
                }
                throw IllegalStateException(uniqueCards.toString())
            }

            3 -> {
                if (uniqueCards[0] == 3 && uniqueCards[1] == 1 && uniqueCards[2] == 1 ||
                 uniqueCards[0] == 2 && uniqueCards[1] == 1 && uniqueCards[2] == 1 && jokers == 1 ||
                 uniqueCards[0] == 1 && uniqueCards[1] == 1 && uniqueCards[2] == 1 && jokers == 2) {
                    return HandType.THREE_OF_A_KIND
                }
                if (uniqueCards[0] == 2 && uniqueCards[1] == 2 && uniqueCards[2] == 1 ||
                  uniqueCards[0] == 2 && uniqueCards[1] == 1 && uniqueCards[2] == 1 || jokers == 1) {
                    return HandType.TWO_PAIR
                }
                throw IllegalStateException(uniqueCards.toString())
            }

            4 -> {
                if(jokers == 0) {
                    return HandType.ONE_PAIR
                }else if(jokers == 1){
                    return HandType.ONE_PAIR
                }
                throw IllegalStateException(uniqueCards.toString())
            }

            else -> {
                when (jokers) {
                    5, 4 -> {
                        return HandType.FIVE_OF_A_KIND
                    }
                    3 -> {
                        return HandType.FOUR_OF_A_KIND
                    }
                    2 -> {
                        return HandType.THREE_OF_A_KIND
                    }
                    1 -> {
                        return HandType.TWO_PAIR
                    }
                    else -> {
                        return HandType.HIGH_CARD
                    }
                }
            }
        }
    }
}

fun main() {
    val data = File("data/day7.txt").readLines()

    val result = data
        .map {
            CamelCard2(it.substringBefore(" "), it.substringAfter(" ").toInt())
        }
        .sortedWith(CamelCard2.HandComparator())
        .mapIndexed { index, it ->
            it.bid * (index + 1)
        }
        .sum()

    println(result)
}

