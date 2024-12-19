package days.aoc2024

import days.Day

class Day19 : Day(2024, 19) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val towels = input.first().split(", ")

        return input.drop(2).count { design -> isDesignPossible(design, towels) }
    }

    private fun isDesignPossible(design: String, towelsAvailable: List<String>): Boolean {
        return Regex("(${towelsAvailable.joinToString("|")})+").matches(design)
    }

    fun calculatePartTwo(input: List<String>): Long {
        val towels = input.first().split(", ")

        return input.drop(2).sumOf { design -> possibleDesignCount(design, towels, mutableMapOf()) }
    }

    private fun possibleDesignCount(design: String, towelsAvailable: List<String>, cache: MutableMap<String,Long>): Long {
        return if (design.isEmpty()) {
            1
        } else {
            cache.getOrPut(design) {
                val matchingTowels = towelsAvailable.filter { towel -> design.startsWith(towel) }
                if (matchingTowels.isNotEmpty()) {
                    matchingTowels.sumOf {
                        possibleDesignCount(
                            design.drop(it.length),
                            towelsAvailable,
                            cache
                        )
                    }
                } else {
                    0
                }
            }
        }
    }

}