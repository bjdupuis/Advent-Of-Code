package days.aoc2022

import days.Day
import kotlin.math.abs

class Day9 : Day(2022,9) {
    override fun partOne(): Any {
        return calculatePositionsTailVisits(inputList, 1)
    }

    override fun partTwo(): Any {
        return calculatePositionsTailVisits(inputList, 9)
    }

    fun calculatePositionsTailVisits(input: List<String>, numberOfKnots: Int): Int {
        var head = Point(0,0)
        var knots = Array(numberOfKnots) {
            Point(0,0)
        }
        val tailPositions = mutableSetOf<Point>()

        tailPositions.add(knots.last())

        input.map { line -> line.split(" ") }.forEach { (direction, amount) ->
            repeat(amount.toInt()) {
                when (direction) {
                    "R" -> {
                        head.x++
                    }
                    "L" -> {
                        head.x--
                    }
                    "D" -> {
                        head.y++
                    }
                    "U" -> {
                        head.y--
                    }
                }

                var preceding = head
                knots.forEach { knot ->
                    if (abs(preceding.x - knot.x) > 1 || abs(preceding.y - knot.y) > 1) {
                        if (preceding.x > knot.x) {
                            knot.x++
                        } else if (preceding.x < knot.x) {
                            knot.x--
                        }
                        if (preceding.y > knot.y) {
                            knot.y++
                        } else if (preceding.y < knot.y) {
                            knot.y--
                        }
                    }

                    preceding = knot
                }
                knots.last().let {
                    tailPositions.add(Point(it.x, it.y))
                }
            }
        }

        return tailPositions.size
    }

    data class Point(var x:Int, var y:Int)
}