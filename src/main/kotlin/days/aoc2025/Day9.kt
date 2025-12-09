package days.aoc2025

import days.Day
import util.Point2d
import util.Point2dl
import util.toward
import kotlin.math.abs

class Day9 : Day(2025, 9) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Long {
        val points = input.map {
            val parts = it.split(",")
            Point2dl(parts.first().toLong(), parts.last().toLong())
        }

        var maxArea = 0L
        for (i in 0 .. points.lastIndex - 1) {
            for (j in i + 1..points.lastIndex) {
                val p1 = points[i]
                val p2 = points[j]
                val area = (abs(p1.x - p2.x) + 1) * (abs(p1.y - p2.y) + 1)
                if (area > maxArea) {
                    maxArea = area
                }
            }
        }
        return maxArea
    }

    fun calculatePartTwo(input: List<String>): Long {
        val points = input.map {
            val parts = it.split(",")
            Point2dl(parts.first().toLong(), parts.last().toLong())
        }

        var maxArea = 0L
        for (i in 0 .. points.lastIndex - 1) {
            for (j in i + 1..points.lastIndex) {
                val p1 = points[i]
                val p2 = points[j]

                if (!lineCrossesEdges(p1, p2, points)) {
                    // check of the "opposite" points cross an edge... if so, it's "outside" the polygon
                    val o1 = Point2dl(p1.x, p2.y)
                    val o2 = Point2dl(p2.x, p1.y)
                    if (!lineCrossesEdges(o1, o2, points)) {
                        val area = (abs(p1.x - p2.x) + 1) * (abs(p1.y - p2.y) + 1)
                        if (area > maxArea) {
                            println("Line from ($p1,$p2) has area $area")
                            maxArea = area
                        }
                    }
                }

            }
        }
        return maxArea
    }

    private fun lineCrossesEdges(
        p1: Point2dl,
        p2: Point2dl,
        points: List<Point2dl>
    ): Boolean {
        for (i in 0 .. points.lastIndex - 1) {
            val p3 = points[i]
            val p4 = points[i+1]
            if (doIntersect(p1, p2, p3, p4)) {
                return true
            }
        }
        return false
    }

    fun orientation(p: Point2dl, q: Point2dl, r: Point2dl): Int {
        val val1 = (q.y - p.y) * (r.x - q.x)
        val val2 = (q.x - p.x) * (r.y - q.y)
        return when {
            val1 == val2 -> 0 // Collinear
            val1 > val2 -> 1  // Clockwise
            else -> 2         // Counterclockwise
        }
    }

    fun doIntersect(p1: Point2dl, q1: Point2dl, p2: Point2dl, q2: Point2dl): Boolean {
        val o1 = orientation(p1, q1, p2)
        val o2 = orientation(p1, q1, q2)
        val o3 = orientation(p2, q2, p1)
        val o4 = orientation(p2, q2, q1)

        if (p1 == p2 || p1 == q2 || q1 == p2 || q1 == q2) {
            return false
        }

        // General case
        if (o1 != 0 && o1 != o2 && o3 != 0 && o3 != o4) {
            return true
        }

        return false
    }
}