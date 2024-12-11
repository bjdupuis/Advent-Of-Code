package days.aoc2024

import days.Day
import util.isEven

class Day11 : Day(2024, 11) {
    override fun partOne(): Any {
        return calculatePartOne(inputString)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputString)
    }

    fun calculatePartOne(inputString: String): Long {
        return calculateStonesForBlinks(inputString, 25)
    }

    fun calculateStonesForBlinks(input: String, blinks: Int): Long {
        val cache = mutableMapOf<Pair<Int,String>, Long>()
        return input.split(" ").sumOf {
            calculateStonesForBlinksLeft(cache, blinks, it)
        }
    }

    private fun calculateStonesForBlinksLeft(cache: MutableMap<Pair<Int,String>,Long>, blinksLeft: Int, stone: String): Long {
        if (blinksLeft == 0) {
            return 1
        }
        return cache.getOrPut(Pair(blinksLeft, stone)) {
            when {
                stone == "0" ->
                    calculateStonesForBlinksLeft(cache, blinksLeft - 1, "1")
                stone.length.isEven() ->
                    calculateStonesForBlinksLeft(cache, blinksLeft - 1, stone.substring(0, stone.length / 2)) +
                            calculateStonesForBlinksLeft(cache, blinksLeft - 1, stone.substring(stone.length / 2, stone.length).dropWhile { it == '0' }.let { it.ifEmpty { "0" } } )
                else ->
                    calculateStonesForBlinksLeft(cache, blinksLeft - 1, (stone.toLong() * 2024L).toString())
            }
        }
    }

    fun calculatePartTwo(inputString: String): Long {
        return calculateStonesForBlinks(inputString, 75)
    }
}