package days.aoc2024

import days.Day
import util.CharArray2d
import util.Point2d

class Day6 : Day(2024, 6) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val map = CharArray2d(input)
        return findAllVisitedSpaces(map).size
    }

    private fun findAllVisitedSpaces(map: CharArray2d): Set<Point2d> {
        val visited = mutableSetOf<Point2d>()
        var currentPosition = map.findFirst('^')!!
        var direction = Point2d.Direction.North
        var done = false
        while (!done) {
            visited.add(currentPosition)
            val neighbor = currentPosition.neighborInDirection(direction)
            if (neighbor.isWithin(map)) {
                if (map[neighbor] == '#') {
                    direction = direction.turnRight()
                } else {
                    currentPosition += direction.delta
                }
            } else {
                done = true
            }
        }

        return visited
    }

    fun calculatePartTwo(input: List<String>): Int {
        val map = CharArray2d(input)
        val visitedSpaces = findAllVisitedSpaces(map)

        return visitedSpaces.filter { isCreatesLoopIfBlocked(map, it) }.size
    }

    private fun isCreatesLoopIfBlocked(map: CharArray2d, pointToBlock: Point2d): Boolean {
        val visited = mutableSetOf<Pair<Point2d, Point2d.Direction>>()
        var currentPosition = map.findFirst('^')!!
        var direction = Point2d.Direction.North
        var done = false
        while (!done) {
            if (!visited.add(Pair(currentPosition,direction))) {
                return true
            }
            val neighbor = currentPosition.neighborInDirection(direction)
            if (neighbor.isWithin(map)) {
                if (map[neighbor] == '#' || neighbor == pointToBlock) {
                    direction = direction.turnRight()
                } else {
                    currentPosition += direction.delta
                }
            }
            else {
                done = true
            }
        }

        return false
    }
}