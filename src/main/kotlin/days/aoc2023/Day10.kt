package days.aoc2023

import days.Day
import util.Array2d
import util.Point2d

class Day10 : Day(2023, 10) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        var s1 = input.mapIndexedNotNull { index, line ->
            if (line.contains('S')) Point2d(
                line.indexOf('S'), index
            ) else null
        }.first()
        var s2 = s1

        val visited = mutableSetOf<Point2d>()
        visited.add(s1)
        var distance = 0
        do {
            s1 = s1.neighbors().first { s1.isConnectedTo(it, input) && !visited.contains(it) }
            visited.add(s1)
            s2 = s2.neighbors()
                .firstOrNull() { s2.isConnectedTo(it, input) && !visited.contains(it) }
                ?: return distance + 1
            visited.add(s2)
            distance++
        } while (s1 != s2)

        throw IllegalStateException("Didn't find it")
    }

    private fun startReplacement(start: Point2d, input: List<String>): Char {
        val fittings = start.neighbors().filter { start.isConnectedTo(it, input) }
        return when (fittings[0]) {
            start.northernNeighbor -> when (fittings[1]) {
                start.easternNeighbor -> 'L'
                start.southernNeighbor -> '|'
                start.westernNeighbor -> 'J'
                else -> {
                    throw IllegalStateException("nope")
                }
            }

            start.southernNeighbor -> when (fittings[1]) {
                start.easternNeighbor -> 'F'
                start.northernNeighbor -> '|'
                start.westernNeighbor -> '7'
                else -> {
                    throw IllegalStateException("nope")
                }
            }

            start.easternNeighbor -> when (fittings[1]) {
                start.northernNeighbor -> 'L'
                start.southernNeighbor -> 'F'
                start.westernNeighbor -> '-'
                else -> {
                    throw IllegalStateException("nope")
                }
            }

            start.westernNeighbor -> when (fittings[1]) {
                start.northernNeighbor -> 'J'
                start.southernNeighbor -> '7'
                start.easternNeighbor -> '-'
                else -> {
                    throw IllegalStateException("nope")
                }
            }

            else -> {
                throw IllegalStateException("nope")
            }
        }
    }

    fun calculatePartTwo(input: List<String>): Int {
        val start = input.mapIndexedNotNull { index, line ->
            if (line.contains('S')) Point2d(
                line.indexOf('S'), index
            ) else null
        }.first()

        // convert the 'S' character into the appropriate pipe fitting
        val convertedInput = mutableListOf<String>()
        input.forEachIndexed { index, line ->
            if (line.contains('S')) {
                val start = Point2d(line.indexOf('S'), index)
                convertedInput.add(line.replace('S', startReplacement(start, input)))
            } else {
                convertedInput.add(line)
            }
        }

        var s1 = start
        var s2: Point2d? = s1
        val pipe = mutableSetOf<Point2d>()
        pipe.add(s1)
        var done = false
        do {
            s1 = s1.neighbors().first { s1.isConnectedTo(it, input) && !pipe.contains(it) }
            pipe.add(s1)
            s2 = s2!!.neighbors()
                .firstOrNull() { s2!!.isConnectedTo(it, input) && !pipe.contains(it) }
            if (s2 != null) {
                pipe.add(s2)
            } else {
                done = true
            }
        } while (!done)

        // expand the map into 3x3
        val expanded = Array2d(input.first().length * 3, input.size * 3, '.')
        convertedInput.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val point = Point2d(x, y)
                if (pipe.contains(point)) {
                    addFitting(expanded, point, c)
                }
            }
        }

        //flood fill the new map
        val outside = mutableSetOf<Point2d>()
        val stack = ArrayDeque<Point2d>()
        stack.add(Point2d(0, 0))
        while (stack.isNotEmpty()) {
            stack.removeFirst().neighbors().filter { it.isWithin(expanded) && !outside.contains(it) }.forEach {
                if ('#' != expanded[it]) {
                    outside.add(it)
                    stack.add(it)
                }
            }
        }

        var insideCount = 0
        for (y in 0 until expanded.height / 3) {
            for (x in 0 until expanded.width / 3) {
                val point = Point2d(x,y)
                if (isInside(expanded, outside, point)) {
                    insideCount++
                }
            }
        }

        /*
        for (y in 0 until expanded.height) {
            for (x in 0 until expanded.width) {
                val point = Point2d(x,y)
                if (outside.contains(point)) {
                    print('O')
                } else {
                    print(expanded[point])
                }
            }
            println()
        }
         */

        return insideCount
    }

    private fun addFitting(expanded: Array2d<Char>, point: Point2d, c: Char) {
        val map = mapFromFittingToExpanded[c]!!
        map.forEachIndexed { y, chars ->
            chars.forEachIndexed { x, c ->
                expanded[point.copy(x = (point.x * 3) + x, y = (point.y * 3) + y)] = c
            }
        }
    }

    private fun isInside(expanded: Array2d<Char>, outside: Set<Point2d>, point: Point2d): Boolean {
        for (y in point.y * 3 until point.y * 3 + 3) {
            for (x in point.x * 3 until point.x * 3 + 3) {
                val point = Point2d(x,y)

                // basically, if any part of the 3x3 square is either "outside" or is not empty space
                // (e.g. a pipe segment) it's an "inside" block
                if (outside.contains(point)) {
                    return false
                } else if ('.' != expanded[point]) {
                    return false
                }
            }
        }

        return true
    }

    private val mapFromFittingToExpanded = mapOf(
        '|' to arrayOf(
            arrayOf('.', '#', '.'),
            arrayOf('.', '#', '.'),
            arrayOf('.', '#', '.')
        ),
        '-' to arrayOf(
            arrayOf('.', '.', '.'),
            arrayOf('#', '#', '#'),
            arrayOf('.', '.', '.')
        ),
        '7' to arrayOf(
            arrayOf('.', '.', '.'),
            arrayOf('#', '#', '.'),
            arrayOf('.', '#', '.')
        ),
        'J' to arrayOf(
            arrayOf('.', '#', '.'),
            arrayOf('#', '#', '.'),
            arrayOf('.', '.', '.')
        ),
        'L' to arrayOf(
            arrayOf('.', '#', '.'),
            arrayOf('.', '#', '#'),
            arrayOf('.', '.', '.')
        ),
        'F' to arrayOf(
            arrayOf('.', '.', '.'),
            arrayOf('.', '#', '#'),
            arrayOf('.', '#', '.')
        ),
    )
}

fun Point2d.isConnectedTo(other: Point2d, input: List<String>) =
    if (!other.isWithin(input)) {
        false
    } else if (other.y < y) {
        // our northern neighbor
        input[y][x] in listOf('S', '|', 'J', 'L') && input[other.y][other.x] in listOf('|', '7', 'F')
    } else if (other.y > y) {
        // our southern neighbor
        input[y][x] in listOf('S', '|', '7', 'F') && input[other.y][other.x] in listOf('|', 'J', 'L')
    } else if (other.x < x) {
        // our western neighbor
        input[y][x] in listOf('S', '-', '7', 'J') && input[other.y][other.x] in listOf('-', 'F', 'L')
    } else {
        // our eastern neighbor
        input[y][x] in listOf('S', '-', 'F', 'L') && input[other.y][other.x] in listOf('-', '7', 'J')
    }
