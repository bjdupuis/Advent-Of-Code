package days.aoc2022

import days.Day

class Day2 : Day(2022, 2) {
    override fun partOne(): Any {
        return calculateScoreOfGame1(inputList)
    }

    override fun partTwo(): Any {
        return calculateScoreOfGame2(inputList)
    }

    fun calculateScoreOfGame1(input: List<String>): Int {
        return input.sumOf { line ->
            when (line) {
                "A X" -> 1 + 3
                "A Y" -> 2 + 6
                "A Z" -> 3 + 0
                "B X" -> 1 + 0
                "B Y" -> 2 + 3
                "B Z" -> 3 + 6
                "C X" -> 1 + 6
                "C Y" -> 2 + 0
                "C Z" -> 3 + 3
                else -> 0
            }.toInt()
        }
    }

    fun calculateScoreOfGame2(input: List<String>): Int {
        return input.sumOf { line ->
            when (line) {
                "A X" -> 3 + 0
                "A Y" -> 1 + 3
                "A Z" -> 2 + 6
                "B X" -> 1 + 0
                "B Y" -> 2 + 3
                "B Z" -> 3 + 6
                "C X" -> 2 + 0
                "C Y" -> 3 + 3
                "C Z" -> 1 + 6
                else -> 0
            }.toInt()
        }
    }
}