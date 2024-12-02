package days.aoc2024

import days.Day
import kotlin.math.abs

class Day1 : Day(2024, 1) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val folded = input.fold(Pair<List<Int>, List<Int>>(listOf(), listOf())) { acc, line ->
            val ints = line.split(Regex("\\s+")).map(String::toInt)
            Pair((acc.first + ints[0]).sorted(), (acc.second + ints[1]).sorted())
        }
        return folded.first.foldIndexed(0) { index, acc, i ->
            acc + abs(i - folded.second[index])
        }
    }

    fun calculatePartTwo(input: List<String>): Int {
        val folded = input.fold(Pair<List<Int>, List<Int>>(listOf(), listOf())) { acc, line ->
            val ints = line.split(Regex("\\s+")).map(String::toInt)
            Pair(acc.first + ints[0], acc.second + ints[1])
        }
        return folded.first.sumOf { first ->
            first * folded.second.count { it == first }
        }
    }
}