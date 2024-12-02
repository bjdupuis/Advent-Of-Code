package days.aoc2024

import days.Day
import kotlin.math.abs

class Day2 : Day(2024, 2) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        return input.count { line ->
            isSafe(line)
        }
    }

    fun calculatePartTwo(input: List<String>): Int {
        return input.count { line ->
            isSafe(line) || makeSafe(line)
        }
    }

    private fun makeSafe(line: String): Boolean {
        val values = line.split(" ").map(String::toInt)
        for (i in values.indices) {
            val newLine = values.filterIndexed { index, _ -> index != i }.joinToString(" ")
            if (isSafe(newLine)) {
                return true
            }
        }

        return false
    }

    private fun isSafe(line: String): Boolean {
        val values = line.split(" ").map(String::toInt)
        var isGrowing: Boolean? = null
        for (i in 0 until values.size - 1) {
            val delta = values[i] - values[i + 1]
            if (abs(delta) > 3 || delta == 0) {
                return false
            }
            if (isGrowing == null) {
                isGrowing = delta > 0
            } else if (isGrowing && delta < 0 || !isGrowing && delta > 0) {
                return false
            }
        }

        return true
    }
}