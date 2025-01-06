package days.aoc2024

import days.Day
import util.Pathfinding
import util.Pathfinding.NeighborFilter
import util.Point2d

class Day21 : Day(2024, 21) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    /*
+---+---+---+
| 7 | 8 | 9 |
+---+---+---+
| 4 | 5 | 6 |
+---+---+---+
| 1 | 2 | 3 |
+---+---+---+
    | 0 | A |
    +---+---+

    +---+---+
    | ^ | A |
+---+---+---+
| < | v | > |
+---+---+---+
     */
    private val numericKeypad = mapOf(
        Point2d(0,0) to '7', Point2d(1, 0) to '8', Point2d(2, 0) to '9',
        Point2d(0,1) to '4', Point2d(1, 1) to '5', Point2d(2, 1) to '6',
        Point2d(0,2) to '1', Point2d(1, 2) to '2', Point2d(2, 2) to '3',
                                   Point2d(1, 3) to '0', Point2d(2, 3) to 'A',

    )

    private val directionalKeypad = mapOf(
                                   Point2d(1, 0) to '^', Point2d(2, 0) to 'A',
        Point2d(0,1) to '<', Point2d(1, 1) to 'v', Point2d(2, 1) to '>',
    )

    private val numericPaths = shortestPathsFor(numericKeypad)
    private val directionalPaths = shortestPathsFor(directionalKeypad)

    // find the shortest paths from each key to every other key
    private fun shortestPathsFor(keypad: Map<Point2d, Char>): Map<Char,Map<Char,List<String>>> {
        val map = mutableMapOf<Char,Map<Char,MutableList<String>>>()
        for (pad in keypad.keys) {
            val paths = mutableMapOf<Char,MutableList<String>>()
            paths[keypad[pad]!!] = mutableListOf("")
            val frontier = mutableListOf(pad to "")
            val visited = mutableSetOf<Point2d>()

            while (frontier.isNotEmpty()) {
                val (current, path) = frontier.removeFirst()
                visited += current

                current.neighbors().filter { keypad.keys.contains(it) && !visited.contains(it) }.forEach { neighbor ->
                    val pathTo = path + when(current.directionTo(neighbor)) {
                        Point2d.Direction.East -> '>'
                        Point2d.Direction.West -> '<'
                        Point2d.Direction.North -> '^'
                        Point2d.Direction.South -> 'v'
                        else -> throw IllegalStateException("impossible direction")
                    }
                    frontier.add(neighbor to pathTo)
                    paths.getOrPut(keypad.getValue(neighbor)) { mutableListOf() } += pathTo
                }
            }

            map[keypad[pad]!!] = paths
        }

        return map
    }

    private fun calculatePathLength(path: String, depth: Int, paths: Map<Char, Map<Char,List<String>>> = numericPaths, cache: MutableMap<Pair<String,Int>,Long>): Long {
        return cache.getOrPut(Pair(path, depth)) {
            if (depth == 0) {
                path.length.toLong()
            } else {
                "A$path".zipWithNext().sumOf { (first, second) ->
                    paths[first]!![second]!!.minOf { newPath ->
                        calculatePathLength("${newPath}A", depth - 1, directionalPaths, cache)
                    }
                }
            }
        }
    }

    fun calculatePartOne(input: List<String>): Long {
        return input.sumOf { line ->
            calculatePathLength(line, 3, cache = mutableMapOf()) * line.dropLast(1).toLong()
        }
    }

    fun calculatePartTwo(input: List<String>): Long {
        return input.sumOf { line ->
            calculatePathLength(line, 26, cache = mutableMapOf()) * line.dropLast(1).toLong()
        }
    }
}