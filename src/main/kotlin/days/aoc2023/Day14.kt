package days.aoc2023

import days.Day
import util.CharArray2d
import util.Point2d
import util.downUntil

class Day14 : Day(2023, 14) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val rocks = CharArray2d(input)
        tiltPlatform(rocks, Point2d(0,-1))
        return totalRockWeight(rocks)
    }

    private fun totalRockWeight(rocks: CharArray2d): Int {
        var sum = 0
        for (y in 0 until rocks.height) {
            for (x in 0 until rocks.width) {
                val point = Point2d(x, y)
                if (rocks[point] == 'O') {
                    sum += rocks.height - y
                }
            }
        }

        return sum
    }

    private fun tiltPlatform(rocks: CharArray2d, delta: Point2d) {
        val xRange = if (delta.x > 0) rocks.width downTo 0 else 0 until rocks.width
        val yRange = if (delta.y > 0) rocks.height downTo 0 else 0 until rocks.height

        for (y in yRange) {
            for (x in xRange) {
                var point = Point2d(x, y)
                while (point.plus(delta).isWithin(rocks) && rocks[point] == 'O' && rocks[point.plus(delta)] == '.') {
                    rocks[point] = '.'
                    point = point.plus(delta)
                    rocks[point] = 'O'
                }
            }
        }
    }

    private val deltas = listOf(
        Point2d(0, -1),
        Point2d(-1, 0),
        Point2d(0, 1),
        Point2d(1, 0)
    )
    private fun spinCycle(rocks: CharArray2d): CharArray2d {
        val spun = rocks.clone() as CharArray2d
        for (delta in deltas) {
            tiltPlatform(spun, delta)
        }

        return spun
    }

    fun calculatePartTwo(input: List<String>): Int {
        var rocks = CharArray2d(input)

        val memoize = mutableMapOf<Int,Pair<CharArray2d,Int>>()
        var result = 0
        var startOfCycle = -1
        var keyAtStart = 0
        for (i in 1 .. 1000000000) {
            if (i % 1000000 == 0) {
                println("Completed $i iterations... cache has ${memoize.size} entries")
            }

            // see if we've wrapped around the cycle
            if (rocks.hashCode() == keyAtStart) {
                // we have the index of the start of a cycle, we have the length of the cycle.
                // If we weren't a caveman we should be able to math this out.
                val cycleSize = i - startOfCycle
                val additionalCyclesNeeded = ((1000000000 - i) % cycleSize) + 1
                for (remaining in 1 .. additionalCyclesNeeded) {
                    val cache = memoize.getOrPut(rocks.hashCode()) {
                        val spun = spinCycle(rocks)
                        Pair(spun, totalRockWeight(spun))
                    }
                    rocks = cache.first
                    result = cache.second
                }

                return result
            } else if (memoize.containsKey(rocks.hashCode()) && startOfCycle < 0) {
                startOfCycle = i
                keyAtStart = rocks.hashCode()
            }
            val cache = memoize.getOrPut(rocks.hashCode()) {
                val spun = spinCycle(rocks)
                Pair(spun, totalRockWeight(spun))
            }
            rocks = cache.first
            result = cache.second

        }

        return result
    }
}