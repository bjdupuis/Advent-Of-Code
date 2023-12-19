package days.aoc2023

import days.Day
import util.CharArray2d
import util.Pathfinding
import util.Point2d

class Day17 : Day(2023, 17) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val map = CharArray2d(input)
        val start = Point2d(0, 0)
        val finish = Point2d(map.maxColumnIndex, map.maxRowIndex)

        data class State(val point: Point2d, val direction: Point2d.Direction, val consecutiveMovesInDirection: Int)

        val startState = State(start, Point2d.Direction.East, 1)

       return Pathfinding<State>().dijkstraShortestPath(
            start = startState,
            neighborIterator = { current ->
                current.point.neighbors().filter {
                    current.direction != it.directionTo(current.point)
                }.mapNotNull { neighbor ->
                    val consecutiveCount = if (current.direction == current.point.directionTo(neighbor)) {
                        if (current.consecutiveMovesInDirection == 3) {
                            null
                        } else {
                            current.consecutiveMovesInDirection + 1
                        }
                    } else {
                        1
                    }
                    if (consecutiveCount != null) {
                        State(neighbor, current.point.directionTo(neighbor)!!, consecutiveCount)
                    } else {
                        null
                    }
                }
            },
            neighborFilter = { it.point.isWithin(map) },
            edgeCost = { _, destination -> map[destination.point].digitToInt() },
            terminationCondition = { it.point == finish }
        )
    }


    fun calculatePartTwo(input: List<String>): Int {
        val map = CharArray2d(input)
        val start = Point2d(0, 0)
        val finish = Point2d(map.maxColumnIndex, map.maxRowIndex)

        data class State(val point: Point2d, val direction: Point2d.Direction, val consecutiveMovesInDirection: Int)

        val startState = State(start, Point2d.Direction.East, 1)

        return Pathfinding<State>().dijkstraShortestPath(
            start = startState,
            neighborIterator = { current ->
                current.point.neighbors().filter {
                    current.direction != it.directionTo(current.point)
                }.mapNotNull { neighbor ->
                    val consecutiveCount = if (current.direction == current.point.directionTo(neighbor)) {
                        if (current.consecutiveMovesInDirection == 10) {
                            null
                        } else {
                            current.consecutiveMovesInDirection + 1
                        }
                    } else if (current.consecutiveMovesInDirection < 4) {
                        null
                    } else {
                        1
                    }
                    if (consecutiveCount != null) {
                        State(neighbor, current.point.directionTo(neighbor)!!, consecutiveCount)
                    } else {
                        null
                    }
                }
            },
            neighborFilter = { it.point.isWithin(map) },
            edgeCost = { _, destination -> map[destination.point].digitToInt() },
            terminationCondition = { it.point == finish && it.consecutiveMovesInDirection >= 4 }
        )
    }
}