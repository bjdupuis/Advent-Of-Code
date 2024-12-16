package days.aoc2024

import days.Day
import util.CharArray2d
import util.Pathfinding
import util.Point2d
import kotlin.io.path.Path

class Day16 : Day(2024, 16) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    data class PointWithBearing(val position: Point2d, val bearing: Point2d.Direction)
    fun calculatePartOne(input: List<String>): Int {
        val map = CharArray2d(input)
        val pathfinding = Pathfinding<PointWithBearing>()
        val shortest = pathfinding.dijkstraShortestPath(
            PointWithBearing(map.findFirst('S')!!, Point2d.Direction.East),
            { current -> current.position.neighbors().map { PointWithBearing(it, current.position.directionTo(it)!!) } },
            { current, neighbor ->
                neighbor.position.isWithin(map) && map[neighbor.position] != '#' &&
                    neighbor.position != current.position + current.bearing.opposite().delta
            },
            { current, neighbor ->
                if (current.bearing != current.position.directionTo(neighbor.position)) {
                    1001
                } else {
                    1
                }
            },
            { current -> map[current.position] == 'E'}
        )
        return shortest
    }

    fun calculatePartTwo(input: List<String>): Int {
        val map = CharArray2d(input)
        val pathfinding = Pathfinding<PointWithBearing>()
        val shortest = pathfinding.dijkstraShortestPath(
            PointWithBearing(map.findFirst('S')!!, Point2d.Direction.East),
            { current -> current.position.neighbors().map { PointWithBearing(it, current.position.directionTo(it)!!) } },
            { current, neighbor ->
                neighbor.position.isWithin(map) && map[neighbor.position] != '#' &&
                        neighbor.position != current.position + current.bearing.opposite().delta
            },
            { current, neighbor ->
                if (current.bearing != current.position.directionTo(neighbor.position)) {
                    1001
                } else {
                    1
                }
            },
            { current -> map[current.position] == 'E'}
        )

        val paths = pathfinding.findAllPathsDfsRecursive(
            PointWithBearing(map.findFirst('S')!!, Point2d.Direction.East),
            { current -> current.position.neighbors().map { PointWithBearing(it, current.position.directionTo(it)!!) } },
            { current, neighbor ->
                neighbor.position.isWithin(map) && map[neighbor.position] != '#' &&
                        neighbor.position != current.position + current.bearing.opposite().delta
            },
            { current, neighbor ->
                if (current.bearing != current.position.directionTo(neighbor.position)) {
                    1001
                } else {
                    1
                }
            },
            shortest,
            { current -> map[current.position] == 'E'}
        )

        val reduced = paths.reduce { acc, path -> acc.union(path.toSet()).toList() }.map { it.position }.distinct()

        for (y in map.columnIndices) {
            val row = map.getRow(y)
            for (x in row.indices) {
                if (reduced.any { it == Point2d(x,y) }) {
                    print("O")
                } else {
                    print(row.get(x))
                }
            }
            println()
        }

        return reduced.size
    }

}