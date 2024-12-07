package days.aoc2016

import days.Day

class Day3 : Day(2016, 3) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        return input.filter(String::isNotEmpty).count { isTrianglePossible(it.trim().split(Regex("\\s+")).map(String::toInt)) }
    }

    private fun isTrianglePossible(sides: List<Int>): Boolean {
        return sides[0] + sides[1] > sides[2] && sides[1] + sides[2] > sides[0] && sides[0] + sides[2] > sides[1]
    }

    fun calculatePartTwo(input: List<String>): Int {
        return input.filter(String::isNotEmpty).map { it.trim().split(Regex("\\s+")).map(String::toInt) }.chunked(3).flatMap {
            listOf(
                listOf(it[0][0], it[1][0], it[2][0]),
                listOf(it[0][1], it[1][1], it[2][1]),
                listOf(it[0][2], it[1][2], it[2][2]),
            )
        }.count { isTrianglePossible(it) }
    }
}