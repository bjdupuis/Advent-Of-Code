package days.aoc2024

import days.Day
import util.CharArray2d
import util.Point2d

class Day10 : Day(2024, 10) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val map = CharArray2d(input)
        return map.find('0').sumOf { calculateTrailheadScore(it, map, setOf()).size }
    }

    private fun calculateTrailheadScore(current: Point2d, map: CharArray2d, reachable: Set<Point2d>): Set<Point2d> {
        return if (map[current] == '9') {
            reachable.plus(current)
        } else {
            current.neighbors().filter { it.isWithin(map) && map[it] == map[current] + 1 }.flatMap { calculateTrailheadScore(it, map, reachable) }.toSet()
        }
    }

    fun calculatePartTwo(input: List<String>): Int {
        val map = CharArray2d(input)
        return map.find('0').sumOf { calculateTrailheadRating(it, map) }
    }

    private fun calculateTrailheadRating(current: Point2d, map: CharArray2d): Int {
        return if (map[current] == '9') {
            1
        } else {
            current.neighbors().filter { it.isWithin(map) && map[it] == map[current] + 1 }.sumOf { calculateTrailheadRating(it, map) }
        }
    }
}