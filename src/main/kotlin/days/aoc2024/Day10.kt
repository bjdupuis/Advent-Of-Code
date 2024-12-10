package days.aoc2024

import days.Day
import util.CharArray2d
import util.Pathfinding
import util.Point2d

class Day10 : Day(2024, 10) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val map = CharArray2d(input)
        val pathfinding = Pathfinding<Point2d>()
        return map.find('0').sumOf { start ->
            val visited = mutableSetOf<Point2d>()
            pathfinding.findAllPaths(
                start,
                { it.neighbors() },
                { current, neighbor -> neighbor.isWithin(map) && map[neighbor] == map[current] + 1 },
                { map[it] == '9' && visited.add(it) }
            )
            visited.size
        }
    }

    fun calculatePartTwo(input: List<String>): Int {
        val map = CharArray2d(input)
        val pathfinding = Pathfinding<Point2d>()
        return map.find('0').sumOf { start ->
            pathfinding.findAllPaths(
                start,
                { it.neighbors() },
                { current, neighbor -> neighbor.isWithin(map) && map[neighbor] == map[current] + 1 },
                { map[it] == '9' }
            ).size
        }
    }

}