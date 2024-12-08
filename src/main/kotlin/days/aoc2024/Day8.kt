package days.aoc2024

import days.Day
import util.CharArray2d
import util.Point2d
import util.combinations

class Day8 : Day(2024, 8) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val antinodeLocations = mutableSetOf<Point2d>()
        val map = CharArray2d(input)
        for (c in ('A'..'Z') + ('a'..'z') + ('0'..'9')) {
            map.find(c).asIterable().combinations(2).forEach { combination ->
                val delta = combination[0] - combination[1]
                (combination[0] + delta).let {
                    if (it.isWithin(map)) {
                        antinodeLocations.add(it)
                    }
                }
                (combination[1] - delta).let {
                    if (it.isWithin(map)) {
                        antinodeLocations.add(it)
                    }
                }

            }
        }
        return antinodeLocations.size
    }

    fun calculatePartTwo(input: List<String>): Int {
        val antinodeLocations = mutableSetOf<Point2d>()
        val map = CharArray2d(input)
        for (c in ('A'..'Z') + ('a'..'z') + ('0'..'9')) {
            map.find(c).asIterable().combinations(2).forEach { combination ->
                val delta = combination[0] - combination[1]
                var current = combination[0]
                while (current.isWithin(map)) {
                    antinodeLocations.add(current)
                    current += delta
                }
                current = combination[1]
                while (current.isWithin(map)) {
                    antinodeLocations.add(current)
                    current -= delta
                }
            }
        }
        return antinodeLocations.size
    }
}