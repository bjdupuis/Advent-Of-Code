package days.aoc2025

import days.Day
import util.power
import kotlin.math.pow

class Day3 : Day(2025, 3) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        return input.sumOf { line ->
            val joltages = line.map { c -> c - '0' }
            val max = joltages.dropLast(1).max()
            val joltage = max * 10 + joltages.dropWhile { it != max }.drop(1).max()
            joltage
        }
    }

    fun calculatePartTwo(input: List<String>): Long {
        return input.sumOf { line ->
            val joltages = line.map { c -> c - '0' }
            var total = 0L
            var startingPoint = 0
            for (digits in 11 downTo 0) {
                val max = joltages.drop(startingPoint).dropLast(digits).max()
                val power = 10L.power(digits)
                total += max * power
                startingPoint = joltages.drop(startingPoint).indexOfFirst { it == max } + 1 + startingPoint
            }
            total
        }
    }
}