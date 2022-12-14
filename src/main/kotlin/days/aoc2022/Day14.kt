package days.aoc2022

import days.Day
import java.lang.Integer.max
import java.lang.Integer.min

class Day14 : Day(2022, 14) {
    override fun partOne(): Any {
        return calculateAmountOfSandSettled(inputList)
    }

    override fun partTwo(): Any {
        return calculateSandUntilBlocked(inputList)
    }

    fun calculateAmountOfSandSettled(input: List<String>): Int {
        val cave = Cave(input)

        var sandDropped = 0
        while(cave.holdsDroppedSand()) {
            sandDropped++
        }

        return sandDropped
    }

    fun calculateSandUntilBlocked(input: List<String>): Int {
        val cave = Cave(input, true)

        var sandDropped = 0
        while(cave.holdsDroppedSand()) {
            sandDropped++
        }

        return sandDropped + 1
    }

    class Cave(input: List<String>, withFloor: Boolean = false) {
        fun holdsDroppedSand(): Boolean {
            var sandPosition = Point(500, 0)
            var sandMoving = true
            do {
                try {
                    sandPosition = listOf(
                        Point(sandPosition.x, sandPosition.y + 1),
                        Point(sandPosition.x - 1, sandPosition.y + 1),
                        Point(sandPosition.x + 1, sandPosition.y + 1)
                    ).first {
                        topography[it] == ' '
                    }
                } catch (e: NoSuchElementException) {
                    sandMoving = false
                    topography[sandPosition] = 'o'
                }
            } while (sandMoving && sandPosition.y < bottom && sandPosition.y > 0)

            return sandPosition.y in 1 until bottom
        }

        val bottom: Int
        val topography: Array<Array<Char>>
        init {
            val points = input.flatMap { line ->
                line.split(" -> ").map { pair ->
                    pair.split(",").let { coordinates ->
                        Point(coordinates.first().toInt(), coordinates.last().toInt())
                    }
                }
            }

            bottom = points.maxOf { it.y } + if (withFloor) 2 else 0

            topography = Array(bottom + 1) {
                Array(1000 + 1) { ' ' }
            }

            input.forEach { line ->
                val points = line.split(" -> ").map { pair ->
                    pair.split(",").let { coordinates ->
                        Point(coordinates.first().toInt(), coordinates.last().toInt())
                    }
                }

                points.windowed(2).forEach { points ->
                    val p1 = points.first()
                    val p2 = points.last()

                    if (p1.x == p2.x) {
                        for (y in min(p1.y, p2.y)..max(p1.y, p2.y)) {
                            topography[Point(p1.x, y)] = '#'
                        }
                    } else {
                        for (x in min(p1.x, p2.x)..max(p1.x, p2.x)) {
                            topography[Point(x, p1.y)] = '#'
                        }
                    }
                }
            }

            if (withFloor) {
                for (x in 0..1000) {
                    topography[Point(x, bottom)] = '#'
                }
            }
        }

    }
}

data class Point(val x: Int, val y: Int)

operator fun <T> Array<Array<T>>.get(point: Point): T? {
    return get(point.y)[point.x]
}

operator fun <T> Array<Array<T>>.set(point: Point, value: T) {
    get(point.y)[point.x] = value
}