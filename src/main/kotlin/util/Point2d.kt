package util

import kotlin.math.abs

data class Point2d(val x: Int, val y: Int) {
    fun distanceTo(other: Point2d): Int {
        return abs(other.x - x) + abs(other.y - y)
    }

    operator fun plus(other: Point2d): Point2d {
        return Point2d(x + other.x, y + other.y)
    }

    operator fun minus(other: Point2d): Point2d {
        return Point2d(x - other.x, y - other.y)
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

    fun isWithin(inputList: List<String>): Boolean {
        return y in inputList.indices && x in inputList.first().indices
    }

    fun isWithin(array:Array2d<Char>): Boolean {
        return y in 0 until array.height && x in 0 until array.width
    }

    fun isWithin(array:CharArray2d): Boolean {
        return y in 0 until array.height && x in 0 until array.width
    }

    fun isWithin(upperLeft: Point2d, lowerRight: Point2d): Boolean {
        return x >= upperLeft.x && x <= lowerRight.x && y >= upperLeft.y && y <= lowerRight.y
    }

    fun directionTo(other: Point2d): Direction? {
        return when (other - this) {
            Direction.East.delta -> Direction.East
            Direction.West.delta -> Direction.West
            Direction.North.delta -> Direction.North
            Direction.South.delta -> Direction.South
            else -> null
        }
    }

    val westernNeighbor: Point2d by lazy { this.copy(x = this.x - 1) }
    val easternNeighbor: Point2d by lazy { this.copy(x = this.x + 1) }
    val northernNeighbor: Point2d by lazy { this.copy(y = this.y - 1) }
    val southernNeighbor: Point2d by lazy { this.copy(y = this.y + 1) }

    enum class Direction(val delta: Point2d) {
        North(Point2d(0, -1)),
        South(Point2d(0, 1)),
        East(Point2d(1, 0)),
        West(Point2d(-1, 0));

        fun opposite() =
            when (this) {
                North -> South
                South -> North
                East -> West
                West -> East
            }
    }
}

data class Point2dl(val x: Long, val y: Long) {
    fun distanceTo(other: Point2dl): Long {
        return abs(other.x - x) + abs(other.y - y)
    }

    operator fun plus(other: Point2dl): Point2dl {
        return Point2dl(x + other.x, y + other.y)
    }

    operator fun minus(other: Point2dl): Point2dl {
        return Point2dl(x - other.x, y - other.y)
    }

}