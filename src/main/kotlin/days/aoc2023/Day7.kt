package days.aoc2023

import days.Day

class Day7 : Day(2023, 7) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val hands = input.map { it.split(" ") }.map { Hand(it[0], it[1].toInt())}
        val sortedHands = hands.sorted()
        return sortedHands.mapIndexed { index, hand -> (hands.size - index) * hand.bid }.sum()
    }

    internal class Hand(private val orderedCards: String, val bid: Int): Comparable<Hand> {
        private val conversions = mapOf(
            'A' to 'A',
            'K' to 'B',
            'Q' to 'C',
            'J' to 'D',
            'T' to 'E',
            '9' to 'F',
            '8' to 'G',
            '7' to 'H',
            '6' to 'I',
            '5' to 'J',
            '4' to 'K',
            '3' to 'L',
            '2' to 'M',
            '1' to 'N'
        )
        private val converted = orderedCards.map { conversions[it] ?: throw IllegalStateException() }.joinToString("")
        private val grouped = orderedCards.groupingBy { it }.eachCount().toList().sortedBy { (_, value) -> value }.reversed()
        private val rank = if (grouped.size == 1) {
            1
        } else if (grouped.size == 2) {
            if (grouped.first().second == 4) {
                2
            } else {
                3
            }
        } else if (grouped.size == 3) {
            if (grouped.first().second == 3) {
                4
            } else {
                5
            }
        } else if (grouped.size == 4) {
            6
        } else {
            7
        }

        override fun compareTo(other: Hand): Int {
            return if (rank == other.rank) {
                converted.compareTo(other.converted)
            } else {
                rank.compareTo(other.rank)
            }
        }
    }

    fun calculatePartTwo(input: List<String>): Int {
        val hands = input.map { it.split(" ") }.map { HandWithJokers(it[0], it[1].toInt())}
        val sortedHands = hands.sorted()
        return sortedHands.mapIndexed { index, hand -> (hands.size - index) * hand.bid }.sum()
    }

    internal class HandWithJokers(private val orderedCards: String, val bid: Int): Comparable<HandWithJokers> {
        private val conversions = mapOf(
            'A' to 'A',
            'K' to 'B',
            'Q' to 'C',
            'T' to 'E',
            '9' to 'F',
            '8' to 'G',
            '7' to 'H',
            '6' to 'I',
            '5' to 'J',
            '4' to 'K',
            '3' to 'L',
            '2' to 'M',
            '1' to 'N',
            'J' to 'O'
        )
        private val converted = orderedCards.map { conversions[it] ?: throw IllegalStateException() }.joinToString("")
        private val originalGrouped = orderedCards.groupingBy { it }.eachCount().toList().sortedBy { (_, value) -> value }.reversed()
        private val grouped = if (originalGrouped.size > 1 && originalGrouped.any { it.first == 'J' }) {
            val jokerCount = originalGrouped.first { it.first == 'J' }.second
            val topCard = originalGrouped.first { it.first != 'J' }
            listOf(Pair(topCard.first, topCard.second + jokerCount)) + originalGrouped.filter { it.first != 'J' }.filter { it.first != topCard.first }
        } else {
            originalGrouped
        }
        private val rank = if (grouped.size == 1) {
            1
        } else if (grouped.size == 2) {
            if (grouped.first().second == 4) {
                2
            } else {
                3
            }
        } else if (grouped.size == 3) {
            if (grouped.first().second == 3) {
                4
            } else {
                5
            }
        } else if (grouped.size == 4) {
            6
        } else {
            7
        }

        override fun compareTo(other: HandWithJokers): Int {
            return if (rank == other.rank) {
                converted.compareTo(other.converted)
            } else {
                rank.compareTo(other.rank)
            }
        }
    }
}