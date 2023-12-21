package days.aoc2023

import days.Day
import util.CharArray2d
import util.Point2d

class Day21 : Day(2023, 21) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        return calculatePossiblePlotsForSteps(input, 64)
    }

    fun calculatePossiblePlotsForSteps(input: List<String>, steps: Int): Int {
        val map = CharArray2d(input)
        val start = map.findFirst('S')!!
        var stepsTaken = 0
        var currentLocations = mutableListOf<Point2d>()
        currentLocations.add(start)
        while (stepsTaken < steps) {
            val newLocations = mutableListOf<Point2d>()
            while (currentLocations.isNotEmpty()) {
                newLocations.addAll(currentLocations.removeFirst().neighbors().filter { it.isWithin(map) && (map[it] == '.' || map[it] == 'S') && !newLocations.contains(it) })
            }
            currentLocations = newLocations
            stepsTaken++
        }
        return currentLocations.size
    }

    fun calculatePartTwo(input: List<String>): Long {
        return calculatePossiblePlotsForStepsOnInfinitePlane(input, 26501365)
    }

    fun calculatePossiblePlotsForStepsOnInfinitePlane(input: List<String>, i: Int): Long {

        return 0L
    }
}