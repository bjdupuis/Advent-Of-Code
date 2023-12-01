package days.aoc2023

import days.Day

class Day1: Day(2023, 1) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        return input.sumOf { line ->
            line.firstDigit() * 10 + line.lastDigit()
        }
    }

    fun calculatePartTwo(input: List<String>): Int {
        return input.sumOf { line ->
            val result = line.firstDigit(true) * 10 + line.lastDigit(true)
            println("Result is $result")
            result
        }
    }

}

fun String.firstDigit(includeSpelledOut: Boolean = false): Int {
    val numbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    if (includeSpelledOut) {
        for (i in indices) {
            if (this[i].isDigit()) {
                return this[i].digitToInt()
            } else {
                numbers.forEachIndexed { index, number ->
                    if (i + number.length - 1 in indices) {
                        if (substring(i, i + number.length) == number) {
                            return index + 1
                        }
                    }
                }
            }
        }
    } else {
        return first { it.isDigit() }.digitToInt()
    }

    throw IllegalStateException("That shouldn't happen")
}

fun String.lastDigit(includeSpelledOut: Boolean = false): Int {
    val numbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    if (includeSpelledOut) {
        for (i in indices.reversed()) {
            if (this[i].isDigit()) {
                return this[i].digitToInt()
            } else {
                numbers.forEachIndexed { index, number ->
                    if (i + number.length - 1 in indices) {
                        if (substring(i, i + number.length) == number) {
                            return index + 1
                        }
                    }
                }
            }
        }
    } else {
        return last { it.isDigit() }.digitToInt()
    }
    throw IllegalStateException("That shouldn't happen")
}