package days.aoc2022

import days.Day

class Day6 : Day(2022, 6) {
    override fun partOne(): Any {
        return charactersBeforeStartOfMarker(inputString, 4)
    }

    override fun partTwo(): Any {
        return charactersBeforeStartOfMarker(inputString, 14)
    }

    fun charactersBeforeStartOfMarker(input: String, windowSize: Int): Int {
        input.windowed(windowSize, 1).forEachIndexed { index, s ->
            if (s.toSet().size == windowSize) {
                return index + windowSize
            }
        }

        return 0
    }
}