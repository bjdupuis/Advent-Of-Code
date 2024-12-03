package days.aoc2024

import days.Day

class Day3 : Day(2024, 3) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Long {
        return input.sumOf { line ->
            Regex("mul\\((\\d+),(\\d+)\\)").findAll(line).map { result ->
                result.destructured.let { (first, second) ->
                    first.toLong() * second.toLong()
                }
            }.sum()
        }
    }

    fun calculatePartTwo(input: List<String>): Long {
        var enabled = true
        return input.sumOf { line ->
            Regex("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)").findAll(line).map { result ->
                when (result.value) {
                    "do()" -> {
                        enabled = true
                        0L
                    }

                    "don't()" -> {
                        enabled = false
                        0L
                    }
                    else -> {
                        result.destructured.let { (first, second) ->
                            if (enabled) {
                                first.toLong() * second.toLong()
                            } else {
                                0L
                            }
                        }
                    }
                }
            }.sum()
        }
    }
}