package days.aoc2022

import days.Day
import java.util.*

class Day12 : Day(2022, 12) {
    override fun partOne(): Any {
        return calculateShortestRoute(inputList)
    }

    override fun partTwo(): Any {
        return calculateShortestRouteFromAnyAToEnd(inputList)
    }

    fun calculateShortestRouteFromAnyAToEnd(input: List<String>): Int {
        val forest = parseForest(input)

        var end: Point? = null
        for (y in forest.indices) {
            for (x in forest[y].indices) {
                if (forest[y][x] == 'E') {
                    end = Point(x, y)
                }
            }
        }

        val distances = mutableListOf<Int>()
        for (y in forest.indices) {
            for (x in forest[y].indices) {
                if (forest[y][x] == 'a') {
                    distances.add(findShortestPath(forest, Point(x, y), end!!))
                }
            }
        }

        return distances.minOf { it }
    }

    fun calculateShortestRoute(input: List<String>): Int {
        val forest = parseForest(input)

        var start: Point? = null
        var end: Point? = null
        for (y in forest.indices) {
            for (x in forest[y].indices) {
                if (forest[y][x] == 'S') {
                    start = Point(x, y)
                }
                if (forest[y][x] == 'E') {
                    end = Point(x, y)
                }
            }
        }

        return findShortestPath(forest, start!!, end!!)
    }

    private fun findShortestPath(forest: Array<Array<Char>>, start: Point, end: Point): Int {
        val visited = mutableSetOf<Point>()
        val queue = PriorityQueue<PathElement>()
        queue.add(PathElement(start, 0))

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.point in visited) {
                continue
            }
            visited.add(current.point)
            if (current.point == end) {
                // we did it
                return current.cost
            }
            val currentTree = forest[current.point.y][current.point.x]
            val currentHeight = if (currentTree == 'S') 'a' else currentTree
            current.point.neighbors()
                .filter { it.pointIsValid(forest) && !visited.contains(it) }
                .forEach { neighbor ->
                    val neighboringTree = forest[neighbor.y][neighbor.x]
                    val height = if (neighboringTree == 'E') 'z' else neighboringTree
                    if (height - currentHeight <= 1) {
                        queue.offer(PathElement(neighbor, current.cost + 1))
                    }
                }
        }

        return Int.MAX_VALUE
    }

    private fun parseForest(input: List<String>): Array<Array<Char>> {
        val forest = Array(input.size) {
            Array(input.first().length) { ' ' }
        }

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c -> forest[y][x] = c }
        }

        return forest
    }

    data class PathElement(val point: Point, val cost: Int) : Comparable<PathElement> {
        override fun compareTo(other: PathElement): Int {
            return cost.compareTo(other.cost)
        }
    }

    data class Point(val x:Int, val y:Int) {
        fun neighbors(): List<Point> = listOf(copy(x = x + 1), copy(x = x - 1), copy(y = y + 1), copy(y = y - 1))

        fun pointIsValid(forest: Array<Array<Char>>): Boolean =
            forest.indices.contains(y) && forest[y].indices.contains(x)
    }

}
