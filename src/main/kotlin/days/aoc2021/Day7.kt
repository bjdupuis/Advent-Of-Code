package days.aoc2021

import days.Day
import kotlin.math.absoluteValue

class Day7 : Day(2021, 7) {
    override fun partOne(): Any {
        return calculateCheapestFuel(inputString, true)
    }

    override fun partTwo(): Any {
        return calculateCheapestFuel(inputString, false)
    }

    fun calculateCheapestFuel(input: String, constantRate: Boolean): Int {
        val positions = input.trim().split(",").map { it.toInt() }
        val min = positions.minOrNull()!!
        val max = positions.maxOrNull()!!
        println ("Between $min and $max... average is ${positions.average()} and median is ${positions.sorted()[positions.size/2]}")

        var minValue = 999999999
        var minPosition: Int?
        for (testPosition in min..max) {
            var fuel = 0
            for (current in positions.indices) {
                fuel += if (constantRate)
                    (positions[current] - testPosition).absoluteValue
                else
                    (0..((positions[current] - testPosition).absoluteValue)).sum()
            }
            if (fuel < minValue) {
                minValue = fuel
                minPosition = testPosition
                println ("New minimum found at $minPosition: $minValue")
            }
        }

        return minValue
    }

}