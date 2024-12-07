package days.aoc2024

import days.Day

class Day7 : Day(2024, 7) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Long {
        return input.sumOf { calculateCalibrationResult(it) }
    }

    private fun calculateCalibrationResult(line: String, includeConcat: Boolean = false): Long {
        val (t,n) = Regex("(\\d+): (.*)").matchEntire(line)?.destructured!!
        val testValue = t.toLong()
        val numbers = n.split(" ").map(String::toInt)
        return if (permuteCalculationsOf(testValue, numbers.drop(1), numbers.first().toLong(), includeConcat)) testValue else 0L
    }

    private fun permuteCalculationsOf(testValue: Long, numbersLeft: List<Int>, sum: Long, includeConcat: Boolean): Boolean {
        return if (sum > testValue || numbersLeft.isEmpty()) {
            sum == testValue
        } else {
            permuteCalculationsOf(testValue, numbersLeft.drop(1), sum + numbersLeft.first().toInt(), includeConcat) ||
                    permuteCalculationsOf(testValue, numbersLeft.drop(1), sum * numbersLeft.first().toInt(), includeConcat) ||
                    if (includeConcat)
                        permuteCalculationsOf(testValue, numbersLeft.drop(1), (sum.toString() + numbersLeft.first()).toLong(), true)
                    else
                        false
        }
    }

    fun calculatePartTwo(input: List<String>): Long {
        return input.sumOf { calculateCalibrationResult(it, true) }
    }
}