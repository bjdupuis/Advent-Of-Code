package days.aoc2023

import days.Day
import kotlin.math.pow

class Day4 : Day(2023, 4) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    fun calculatePartOne(inputList: List<String>): Int {
        return inputList.sumOf { line ->
            Card.factory(line).score()
        }
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartTwo(inputList: List<String>): Int {
        val cardCounts = mutableMapOf<Int,Int>()
        inputList.forEach { line ->
            val card = Card.factory(line)
            val matches = card.matches()
            cardCounts[card.cardNumber] = (cardCounts[card.cardNumber] ?: 0) + 1

            for (copy in 1 .. cardCounts[card.cardNumber]!!) {
                for (i in 1..matches) {
                    cardCounts[card.cardNumber + i] = (cardCounts[card.cardNumber + i] ?: 0) + 1
                }
            }
        }

        return cardCounts.values.sum()
    }

    internal class Card(
        val cardNumber: Int,
        private val winningNumbers: Set<Int>,
        private val numbersOnCard: Set<Int>
    ) {
        companion object {
            fun factory(line: String): Card {
                val cardNumber = line.dropWhile { !it.isDigit() }.takeWhile { it.isDigit() }.toInt()
                val split = line.dropWhile { it != ':' }.drop(1).split('|')
                val winningNumbers =
                    split.first().trim().split("\\s+".toRegex()).map { it.trim().toInt() }.toSet()
                val numbersOnCard =
                    split.last().trim().split("\\s+".toRegex()).map { it.trim().toInt() }.toSet()
                return Card(cardNumber, winningNumbers, numbersOnCard)
            }
        }
        fun score(): Int {
            val matches = matches()
            return (2.0.pow(matches - 1)).toInt()
        }

        fun matches() = numbersOnCard.intersect(winningNumbers).size
    }
}