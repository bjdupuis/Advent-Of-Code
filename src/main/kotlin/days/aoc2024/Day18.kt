package days.aoc2024

import days.Day
import util.CharArray2d
import util.Pathfinding
import util.Point2d

class Day18 : Day(2024, 18) {
    override fun partOne(): Any {
        return calculatePartOne(inputList, 71, 1024)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList, 71)
    }

    fun calculatePartOne(input: List<String>, gridSize: Int, fallenBytes: Int): Int {
        val map = CharArray2d(gridSize, gridSize, '.')
        input.take(fallenBytes).map { line ->
            line.split(",").map(String::toInt)
        }.forEach {
            map[Point2d(it[0], it[1])] = '#'
        }

        val pathfinding = Pathfinding<Point2d>()
        return pathfinding.dijkstraShortestPathCost(
            Point2d(0,0),
            { current -> current.neighbors() },
            { _, neighbor -> neighbor.isWithin(map) && map[neighbor] != '#' },
            { _, _ -> 1 },
            { current -> current == Point2d(map.maxColumnIndex, map.maxRowIndex) }
        )
    }

    fun calculatePartTwo(input: List<String>, gridSize: Int): String {
        val map = CharArray2d(gridSize, gridSize, '.')
        val pathfinding = Pathfinding<Point2d>()
        input.map { line ->
            line.split(",").map(String::toInt)
        }.forEach {
            map[Point2d(it[0], it[1])] = '#'

            if (pathfinding.dijkstraShortestPathCost(
                Point2d(0,0),
                { current -> current.neighbors() },
                { _, neighbor -> neighbor.isWithin(map) && map[neighbor] != '#' },
                { _, _ -> 1 },
                { current -> current == Point2d(map.maxColumnIndex, map.maxRowIndex) }
            ) == Int.MAX_VALUE) {
                return "${it[0]},${it[1]}"
            }
        }
        return ""
    }
}