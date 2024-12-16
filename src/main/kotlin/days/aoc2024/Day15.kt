package days.aoc2024

import days.Day
import util.CharArray2d
import util.Point2d
import util.Point2dl

class Day15 : Day(2024, 15) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val map = CharArray2d(input.takeWhile { it.isNotEmpty() })
        val instructions = input.dropWhile { it.isNotEmpty() }.drop(1)
        var robotPosition = map.findFirst('@')!!

        instructions.forEach { it.forEach { movement ->
            robotPosition = moveDelta(map, robotPosition, when (movement) {
                '>' -> Point2d.Direction.East.delta
                '<' -> Point2d.Direction.West.delta
                '^' -> Point2d.Direction.North.delta
                'v' -> Point2d.Direction.South.delta
                else -> throw IllegalStateException("direction is weird: $movement")
            })
        } }

        map.print()
        return map.find('O').map { it.y * 100 + it.x }.sum()
    }

    private fun moveDelta(map: CharArray2d, startingPosition: Point2d, delta: Point2d): Point2d {
        var currentPosition = startingPosition
        var stackSize = 0
        var done = false
        while (!done) {
            when (map[currentPosition + (delta * (stackSize + 1))]) {
                '#' -> return currentPosition
                'O' -> stackSize++
                else -> done = true
            }
        }
        map[currentPosition] = '.'
        currentPosition += delta
        map[currentPosition] = '@'
        for (i in 1 .. stackSize) {
            map[currentPosition + delta * i] = 'O'
        }

        return currentPosition
    }

    fun calculatePartTwo(input: List<String>): Int {
        val unexpandedMap = CharArray2d(input.takeWhile { it.isNotEmpty() })
        val map = CharArray2d(unexpandedMap.width * 2, unexpandedMap.height, '.')
        unexpandedMap.iterator().forEach { originalPoint ->
            when (unexpandedMap[originalPoint]) {
                '#' -> {
                    map[originalPoint.copy(x = originalPoint.x * 2)] = '#'
                    map[originalPoint.copy(x = originalPoint.x * 2 + 1)] = '#'
                }
                '@' -> {
                    map[originalPoint.copy(x = originalPoint.x * 2)] = '@'
                }
                'O' -> {
                    map[originalPoint.copy(x = originalPoint.x * 2)] = '['
                    map[originalPoint.copy(x = originalPoint.x * 2 + 1)] = ']'
                }
            }
        }
        val instructions = input.dropWhile { it.isNotEmpty() }.drop(1)
        var robotPosition = map.findFirst('@')!!

        instructions.forEach { it.forEach { movement ->
            //println("Moving $movement")
            robotPosition = moveDeltaEnlarged(map, robotPosition, when (movement) {
                '>' -> Point2d.Direction.East.delta
                '<' -> Point2d.Direction.West.delta
                '^' -> Point2d.Direction.North.delta
                'v' -> Point2d.Direction.South.delta
                else -> throw IllegalStateException("direction is weird: $movement")
            })
            //map.print()
            //println("--------------------------------------------------------------")
        } }

        map.print()
        return map.find('[').map { it.y * 100 + it.x }.sum()
    }

    private fun moveDeltaEnlarged(map: CharArray2d, startingPosition: Point2d, delta: Point2d): Point2d {
        var currentPosition = startingPosition

        if (delta.y != 0) {
            // moving vertically. The not so easy case.
            val linesToMove = findBoxesThatMoveOnAdjacentLines(map, listOf(listOf(currentPosition)), delta)

            if (linesToMove == null && map[currentPosition + delta] != '.') {
                return currentPosition
            } else {
                linesToMove?.drop(1)?.reversed()?.forEach { points ->
                    points.forEach { point ->
                        map[point + delta] = map[point]
                        map[point] = '.'
                    }
                }
                map[currentPosition] = '.'
                currentPosition += delta
                map[currentPosition] = '@'
            }
        } else {
            // moving horizontally. The easy case.
            var stack = mutableListOf<Char>()
            var done = false
            while (!done) {
                when (map[currentPosition + (delta * (stack.size + 1))]) {
                    '#' -> return currentPosition
                    '[' -> stack.add('[')
                    ']' -> stack.add(']')
                    else -> done = true
                }
            }
            map[currentPosition] = '.'
            currentPosition += delta
            map[currentPosition] = '@'
            for (i in 1 .. stack.size) {
                map[currentPosition + delta * i] = stack[i - 1]
            }

        }

        return currentPosition
    }

    private fun findBoxesThatMoveOnAdjacentLines(map: CharArray2d, points: List<List<Point2d>>, delta: Point2d): List<List<Point2d>>? {
        if (points.last().map { it + delta }.any { map[it] == '#'}) return null

        val newPoints = mutableSetOf<Point2d>()

        points.last().map { it + delta }.filter { map[it] == '[' || map[it] == ']' }.forEach {
            newPoints.add(it)
            if (map[it] == '[')
                newPoints.add(it.copy(x = it.x + 1))
            else
                newPoints.add(it.copy(x = it.x - 1))
        }

        return if (newPoints.isNotEmpty()) findBoxesThatMoveOnAdjacentLines(map, points + listOf(newPoints.toList()), delta) else points
    }

}