package days.aoc2023

import days.Day
import util.CharArray2d
import util.Pathfinding
import util.Point2d

class Day23 : Day(2023, 23) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    class State(val point: Point2d, val direction: Point2d.Direction) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as State

            return point == other.point
        }

        override fun hashCode(): Int {
            return point.hashCode()
        }
    }

    fun calculatePartOne(input: List<String>): Int {
        val map = CharArray2d(input)
        val pathfinding = Pathfinding<State>()
        return pathfinding.longestPath(
            start = State(map.findFirst('.')!!, Point2d.Direction.South),
            neighborIterator = { current -> current.point.neighbors().filter { it.isWithin(map) && map[it] != '#' } .mapNotNull { point ->
                var state: State? = null

                if (current.direction.opposite() == current.point.directionTo(point)) {
                    null
                } else {
                    when (map[point]) {
                        '.' -> {
                            state = State(
                                point,
                                current.point.directionTo(point)
                                    ?: throw IllegalStateException("Weird")
                            )
                        }

                        '>' -> {
                            if (current.point.directionTo(point) == Point2d.Direction.East) {
                                state = State(point, Point2d.Direction.East)
                            }
                        }

                        'v' -> {
                            if (current.point.directionTo(point) == Point2d.Direction.South) {
                                state = State(point, Point2d.Direction.South)
                            }
                        }

                        else -> throw IllegalStateException("shouldn't have gotten that")
                    }
                    state
                }
            } },
            neighborFilter = { _, _ -> true },
            terminationCondition = { it.point.y == map.maxRowIndex })!!.size - 1
    }

    // note: currently takes a very long time. Like more than 12 hours. I got lucky that it found the longest
    // as an interim and the value worked to get the ⭐️.
    fun calculatePartTwo(input: List<String>): Int {
        val map = CharArray2d(input)
        val pathfinding = Pathfinding<State>()
        return pathfinding.longestPath(
            start = State(map.findFirst('.')!!, Point2d.Direction.South),
            neighborIterator = { current -> current.point.neighbors().filter { it.isWithin(map) && map[it] != '#' } .mapNotNull { point ->
                var state: State? = null

                if (current.direction.opposite() == current.point.directionTo(point)) {
                    null
                } else {
                    when (map[point]) {
                        in ".>v" -> {
                            state = State(
                                point,
                                current.point.directionTo(point)
                                    ?: throw IllegalStateException("Weird")
                            )
                        }

                        else -> throw IllegalStateException("shouldn't have gotten that")
                    }
                    state
                }
            } },
            neighborFilter = { _,_ -> true },
            terminationCondition = { it.point.y == map.maxRowIndex })!!.size - 1
    }
}