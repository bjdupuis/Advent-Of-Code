package util

import kotlin.math.abs

data class Point2d(val x: Int, val y: Int) {
    fun distanceTo(other: Point2d): Int {
        return abs(other.x - x) + abs(other.y - y)
    }

    operator fun plus(other: Point2d): Point2d {
        return Point2d(x + other.x, y + other.y)
    }

    fun neighbors() = listOf(
        copy(x = x + 1),
        copy(x = x - 1),
        copy(y = y + 1),
        copy(y = y - 1),
    )

    fun allNeighbors() = listOf(
        copy(x = x - 1, y = y - 1),
        copy(y = y - 1),
        copy(x = x + 1, y = y - 1),
        copy(x = x - 1),
        copy(x = x + 1),
        copy(x = x - 1, y = y + 1),
        copy(y = y + 1),
        copy(x = x + 1, y = y + 1),
    )

}

data class Point2dl(val x: Long, val y: Long) {
    fun distanceTo(other: Point2dl): Long {
        return abs(other.x - x) + abs(other.y - y)
    }
}