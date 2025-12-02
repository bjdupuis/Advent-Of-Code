package days.aoc2025

import days.Day
import util.isEven

class Day2 : Day(2025, 2) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Long {
        var total = 0L

        input.first().split(",").forEach { range ->
            Regex("(\\d+)-(\\d+)").matchEntire(range)?.destructured?.let { (start, end) ->
                val low = start.toLong()
                val high = end.toLong()
                for (i in low .. high) {
                    val s = i.toString()

                    if (s.length.isEven()) {
                        if (s.substring(0, s.length / 2) == s.substring(s.length / 2)) {
                            total += i
                        }
                    }
                }
            }
        }
        return total
    }

    fun calculatePartTwo(input: List<String>): Long {
        var total = 0L

        input.first().split(",").forEach { range ->
            Regex("(\\d+)-(\\d+)").matchEntire(range)?.destructured?.let { (start, end) ->
                val low = start.toLong()
                val high = end.toLong()
                for (i in low .. high) {
                    val s = i.toString()

                    for (iterations in 1 .. s.length / 2) {
                        if (Regex("(${s.substring(0, iterations)})+").matches(s)) {
                            total += i
                            break
                        }
                    }
                }
            }
        }
        return total
    }
}