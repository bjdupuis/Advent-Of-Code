package days.aoc2024

import days.Day
import util.CharArray2d
import util.Pathfinding
import util.Point2d

class Day20 : Day(2024, 20) {
    override fun partOne(): Any {
        return calculatePartOne(inputList, 100)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList, 100)
    }

    fun calculatePartOne(input: List<String>, minCheatTime: Int): Int {
        val map = CharArray2d(input)
        val pathfinding = Pathfinding<Point2d>()
        val path = pathfinding.dijkstraShortestPath(
            map.findFirst('S')!!,
            { current -> current.neighbors() },
            { _, neighbor -> neighbor.isWithin(map) && map[neighbor] != '#' },
            { _, _ -> 1 },
            { current -> map[current] == 'E'}
        )

        return path.first.dropLast(1).sumOf { initialCheatPosition ->
            val cheats = initialCheatPosition.neighbors().filter { map[it] == '#' }
            cheats.count { cheat ->
                val cheatCost = pathfinding.dijkstraShortestPathCost(
                    map.findFirst('S')!!,
                    { current -> current.neighbors() },
                    { current, neighbor ->
                        neighbor.isWithin(map) && (map[neighbor] != '#' || ( current == initialCheatPosition && neighbor == cheat))
                    },
                    { _, _ -> 1 },
                    { current -> map[current] == 'E' }
                )
                path.second - cheatCost >= minCheatTime
            }
        }
    }

    fun calculatePartTwo(input: List<String>, minCheatTime: Int): Int {
        val map = CharArray2d(input)
        val pathfinding = Pathfinding<Point2d>()
        val path = pathfinding.dijkstraShortestPath(
            map.findFirst('S')!!,
            { current -> current.neighbors() },
            { _, neighbor -> neighbor.isWithin(map) && map[neighbor] != '#' },
            { _, _ -> 1 },
            { current -> map[current] == 'E'}
        )

        var validCheats = 0
        path.first.forEachIndexed { index, origin ->
            for (i in index + minCheatTime .. path.first.lastIndex) {
                val other = path.first[i]
                if (origin.distanceTo(other) <= 20 && ((i - index) - origin.distanceTo(other) >= minCheatTime)) {
                    validCheats++
                }
            }
        }

        return validCheats
    }
}