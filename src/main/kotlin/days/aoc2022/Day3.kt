package days.aoc2022

import days.Day

class Day3 : Day(2022, 3) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(inputList: List<String>): Int {
        return inputList
            .map { line -> findCommonProperty(line) }
            .sumOf { property -> priority(property) }
    }

    private fun findCommonProperty(input: String): Char {
        val front = input.substring(0, input.length / 2)
        val back = input.substring(input.length / 2)
        return front.first { current -> back.contains(current) }
    }

    private fun priority(input: Char): Int {
        return when(input) {
            in 'a'..'z' -> input - 'a' + 1
            in 'A'..'Z' -> input - 'A' + 27
            else -> throw IllegalStateException()
        }
    }

    fun calculatePartTwo(inputList: List<String>): Int {
        return findBadgesInGroups(inputList)
            .sumOf { badge -> priority(badge) }
    }

    private fun findBadgesInGroups(input: List<String>): List<Char> {
        return input.chunked(3)
            .map { group ->
                group.first().first { badge ->
                    group[1].contains(badge) && group[2].contains(badge)
                }
            }
    }

}