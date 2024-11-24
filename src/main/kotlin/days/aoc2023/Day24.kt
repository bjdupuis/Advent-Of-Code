package days.aoc2023

import days.Day
import org.jetbrains.kotlinx.multik.ndarray.complex.div
import util.Point2dl
import util.Point3d
import util.Point3dl
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day24 : Day(2023, 24) {
    override fun partOne(): Any {
        return calculatePartOne(inputList, 200000000000000L, 400000000000000L)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    class Hailstone(val currentPosition: Point3dl, val velocity: Point3dl) {
        val slope2d: Double = velocity.y.toDouble() / velocity.x.toDouble()
        val yInt2d: Double = currentPosition.y.toDouble() - slope2d * currentPosition.x

        fun intersectionInFuture2dWith(other: Hailstone): Point2dl? {
            if (abs(slope2d) == abs(other.slope2d)) {
                if (yInt2d == other.yInt2d) {
                    println("$slope2d (going ${velocity.x} and ${velocity.y}) and ${other.slope2d} (going ${other.velocity.x} and ${other.velocity.y})")
                    // ----o-----------------o-----
                    //     3                 2
                    // currentPosition.x + velocity.x * t = other.currentPosition.x + other.velocity.x * t
                    // 0 = other.currentPosition.x + other.velocity.x * t - currentPosition.x - velocity.x * t
                    // 0 = other.currentPosition.x - currentPosition.x + t(other.velocity.x - velocity.x)
                    // t(other.velocity.x - velocity.x) = other.currentPosition.x - currentPosition.x
                    // t = other.currentPosition.x - currentPosition.x / otherVelocity.x - otherVelocity
                    val faster = listOf(this, other).maxBy { abs(it.velocity.x) }
                    val slower = listOf(this, other).minBy { abs(it.velocity.x) }
                    val time = abs((slower.currentPosition.x - faster.currentPosition.x) / (slower.velocity.x - faster.velocity.x))

                    val interceptX =
                        currentPosition.x + velocity.x * time
                    return Point2dl(interceptX, (slope2d * interceptX + yInt2d).toLong())
                } else {
                    return null
                }
            } else {
                val interceptX = (other.yInt2d - yInt2d) / (slope2d - other.slope2d)
                val interceptY = slope2d * interceptX + yInt2d

                if (velocity.x >= 0 && (interceptX < currentPosition.x) ||
                    velocity.x <= 0 && (interceptX > currentPosition.x) ||
                    velocity.y >= 0 && (interceptY < currentPosition.y) ||
                    velocity.y <= 0 && (interceptY > currentPosition.y) ||
                    other.velocity.x >= 0 && (interceptX < other.currentPosition.x) ||
                    other.velocity.x <= 0 && (interceptX > other.currentPosition.x) ||
                    other.velocity.y >= 0 && (interceptY < other.currentPosition.y) ||
                    other.velocity.y <= 0 && (interceptY > other.currentPosition.y)
                ) {
                    return null
                }
                return Point2dl(interceptX.toLong(), interceptY.toLong())
            }
        }
    }

    fun calculatePartOne(input: List<String>, minCoordinate: Long, maxCoordinate: Long): Int {
        val hailstones = input.map { line ->
            line.split("@").let {
                val coords = it.first().trim().split(",").map { it.trim().toLong() }
                val offsets = it.last().trim().split(",").map { it.trim().toLong() }
                Hailstone(Point3dl(coords[0], coords[1], coords[2]), Point3dl(offsets[0], offsets[1], offsets[2]))
            }
        }

        var crossingsInRange = 0
        for (i in 0 until hailstones.lastIndex) {
            for (j in i + 1 .. hailstones.lastIndex) {
                val h1 = hailstones[i]
                val h2 = hailstones[j]

                // y = mx + b
                // m1x + b1 = m2x + b2
                // m1x - m2x = b2 - b1
                // x(m1 - m2) = b2 - b1
                // x = b2 - b1 / m1 - m2
                val intersection = h1.intersectionInFuture2dWith(h2)
                if (intersection != null &&
                    intersection.x >= minCoordinate &&
                    intersection.x <= maxCoordinate &&
                    intersection.y >= minCoordinate &&
                    intersection.y <= maxCoordinate) {
                        crossingsInRange++
                }
            }
        }

        // 17768 is too low
        return crossingsInRange
    }

    fun calculatePartTwo(input: List<String>): Int {
        return 0
    }
}