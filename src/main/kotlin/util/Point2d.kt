package util

import kotlin.math.abs

data class Point2d(val x: Int, val y: Int) {
    fun distanceTo(other: Point2d): Int {
        return abs(other.x - x) + abs(other.y - y)
    }
}