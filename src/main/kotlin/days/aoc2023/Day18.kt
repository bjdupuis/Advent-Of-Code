package days.aoc2023

import days.Day
import util.Point2d
import util.Point2dl
import util.isOdd
import util.toward
import kotlin.math.max
import kotlin.math.min

class Day18 : Day(2023, 18) {
    override fun partOne(): Any {
        return calculatePartOneShoelace(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        var cursor = Point2d(0,0)
        val instructions = input.mapNotNull {
            Regex("(\\w) (\\d+) \\(#(\\w+)\\)").matchEntire(it)?.destructured?.let { (direction, length, color) ->
                DigInstruction(direction, length.toInt(), color)
            }
        }
        instructions.fold(cursor) { cursor, instruction ->
            instruction.calculateRangesFrom(cursor)
        }

        var lowerRight = Point2d(Int.MIN_VALUE, Int.MIN_VALUE)
        var upperLeft = Point2d(Int.MAX_VALUE, Int.MAX_VALUE)
        instructions.forEach { instruction ->
            upperLeft = Point2d(
                min(upperLeft.x, min(instruction.xRange.first, instruction.xRange.last)),
                min(upperLeft.y, min(instruction.yRange.first, instruction.yRange.last)))
            lowerRight = Point2d(
                max(lowerRight.x, max(instruction.xRange.first, instruction.xRange.last)),
                max(lowerRight.y, max(instruction.yRange.first, instruction.yRange.last)))
        }

        fun pointIsOnBoundary(point: Point2d): Boolean {
            for (instruction in instructions) {
                if (point.y in instruction.yRange && point.x in instruction.xRange) {
                    return true
                }
            }

            return false
        }

        fun pointIsInside(point: Point2d): Boolean {
            var crossed = 0
            var horizontalsTraversed = mutableSetOf<DigInstruction>()
            for (x in point.x toward upperLeft.x) {
                for (instruction in instructions) {
                    if (point.y in instruction.yRange && x in instruction.xRange) {
                        if (instruction.isVertical()) {
                            crossed++
                        } else if (instruction.isHorizontal() && !horizontalsTraversed.contains(instruction)) {
                            horizontalsTraversed.add(instruction)
                        }
                    }
                }
            }

            return (crossed + horizontalsTraversed.size).isOdd()
        }

        var startingPoint = Point2d(0, 0)
        outer@for (x in upperLeft.x toward lowerRight.x) {
            for (y in upperLeft.y toward lowerRight.y) {
                startingPoint = Point2d(x, y)
                if (!pointIsOnBoundary(startingPoint) && pointIsInside(startingPoint)) {
                    break@outer
                }
            }
        }

        val frontier = mutableListOf(startingPoint)
        val seen = mutableListOf<Point2d>()
        val inside = mutableSetOf(startingPoint)
        while (frontier.isNotEmpty()) {
            val current = frontier.removeFirst()
            seen.add(current)
            current.allNeighbors().filter { !seen.contains(it) && !frontier.contains(it) }.forEach {
                if (!pointIsOnBoundary(it)) {
                    frontier.add(it)
                }
                inside.add(it)
            }
        }

        return inside.count()
    }


    data class DigInstruction(val direction: String, val length: Int, val color: String) {
        lateinit var xRange: IntProgression;
        lateinit var yRange: IntProgression;

        fun calculateRangesFrom(origin: Point2d): Point2d {
            when (direction) {
                "U" -> {
                    xRange = origin.x .. origin.x
                    yRange = origin.y toward origin.y - length
                }
                "D" -> {
                    xRange = origin.x .. origin.x
                    yRange = origin.y toward origin.y + length
                }
                "R" -> {
                    xRange = origin.x toward origin.x + length
                    yRange = origin.y .. origin.y
                }
                "L" -> {
                    xRange = origin.x toward origin.x - length
                    yRange = origin.y .. origin.y
                }
            }

            return Point2d(xRange.last, yRange.last)
        }

        fun isHorizontal(): Boolean = xRange.first != xRange.last
        fun isVertical(): Boolean = yRange.first != yRange.last

        lateinit var start: Point2dl
        lateinit var end: Point2dl

        fun calculateExtrema(origin: Point2dl): Point2dl {
            start = origin
            when (direction) {
                "U" -> {
                    end = start.copy(y = start.y - length)
                }
                "D" -> {
                    end = start.copy(y = start.y + length)
                }
                "R" -> {
                    end = start.copy(x = start.x + length)
                }
                "L" -> {
                    end = start.copy(x = start.x - length)
                }
            }

            return end
        }

    }

    data class RealDigInstruction(val encoded: String) {
        val length = encoded.take(5).toInt(16)
        val direction = when (encoded.last()) {
            '0' -> 'R'
            '1' -> 'D'
            '2' -> 'L'
            '3' -> 'U'
            else -> throw IllegalStateException("Oops")
        }.toString()

        lateinit var start: Point2dl

        fun calculateExtrema(origin: Point2dl): Point2dl {
            start = origin
            return when (direction) {
                "U" -> {
                    start.copy(y = start.y - length)
                }
                "D" -> {
                    start.copy(y = start.y + length)
                }
                "R" -> {
                    start.copy(x = start.x + length)
                }
                "L" -> {
                    start.copy(x = start.x - length)
                }
                else -> throw IllegalStateException("oh dear")
            }
        }


    }

    fun calculatePartOneShoelace(input: List<String>): Long {
        var cursor = Point2dl(0,0)
        val instructions = input.mapNotNull {
            Regex("(\\w) (\\d+) \\(#(\\w+)\\)").matchEntire(it)?.destructured?.let { (direction, length, color) ->
                DigInstruction(direction, length.toInt(), color)
            }
        }
        instructions.fold(cursor) { cursor, instruction ->
            instruction.calculateExtrema(cursor)
        }

        var area = 0L
        instructions.zipWithNext { a, b ->
            area += (a.start.x * b.start.y) - (a.start.y * b.start.x)
        }
        val last = instructions.last()
        val first = instructions.first()
        //area += (last.start.x * first.start.y) - (last.start.y * first.start.x)

        return (area / 2) + (instructions.sumOf { it.length } / 2) + 1
    }

    fun calculatePartTwo(input: List<String>): Long {
        var cursor = Point2dl(0,0)
        val instructions = input.mapNotNull {
            Regex("(\\w) (\\d+) \\(#(\\w+)\\)").matchEntire(it)?.destructured?.let { (_, _, encoded) ->
                RealDigInstruction(encoded)
            }
        }
        instructions.fold(cursor) { cursor, instruction ->
            instruction.calculateExtrema(cursor)
        }

        var area = 0L
        instructions.zipWithNext { a, b ->
            area += (a.start.x * b.start.y) - (a.start.y * b.start.x)
        }
        val last = instructions.last()
        val first = instructions.first()
        area += (last.start.x * first.start.y) - (last.start.y * first.start.x)

        return return (area / 2) + (instructions.sumOf { it.length } / 2) + 1
    }
}