package days.aoc2023

import days.Day
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import util.Array2d
import util.Point2d

class Day11 : Day(2023, 11) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList, 1000000)
    }

    fun calculatePartOne(input: List<String>): Int {
        val universeMap = Array(input.size) { Array(input.first().length) { ' ' } }
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                universeMap[y][x] = c
            }
        }

        val rowsToExpand = mutableSetOf<Int>()
        val columnsToExpand = mutableSetOf<Int>()
        for (y in universeMap.indices) {
            if (universeMap[y].all { it == '.' }) {
                rowsToExpand.add(y)
            }
        }
        for (x in universeMap.first().indices) {
            if (Array(universeMap.size) { universeMap[it][x] }.all { it == '.'}) {
                columnsToExpand.add(x)
            }
        }

        // keeping this solution for posterity. Do I find it humorous that the time I took diddling
        // with accurately expanding the universe was longer than I took to do the required math
        // for part 2? Absolutely.
        val expandedUniverseMap = Array(universeMap.size + rowsToExpand.size) { Array(universeMap.first().size + columnsToExpand.size) { ' ' } }
        var expandedY = 0
        for (y in universeMap.indices) {
            var finishedProcessingExtraRow = true
            do {
                var expandedX = 0
                for (x in universeMap[y].indices) {
                    var finishedProcessingExtraColumn = true
                    do {
                        expandedUniverseMap[y + expandedY][x + expandedX] = universeMap[y][x]
                        if (x in columnsToExpand) {
                            if (finishedProcessingExtraColumn) {
                                expandedX++
                                finishedProcessingExtraColumn = false
                            } else {
                                finishedProcessingExtraColumn = true
                            }
                        }

                    } while (!finishedProcessingExtraColumn)
                }
                if (y in rowsToExpand) {
                    if (finishedProcessingExtraRow) {
                        expandedY++
                        finishedProcessingExtraRow = false
                    } else {
                        finishedProcessingExtraRow = true
                    }
                }
            } while (!finishedProcessingExtraRow)
        }

        /*
        expandedUniverseMap.forEach { line ->
            line.forEach { print(it) }
            println()
        }
         */

        val galaxies = expandedUniverseMap.flatMapIndexed { y, chars ->
            chars.mapIndexed { x, c -> if (c == '#') Point2d(x, y) else null}
        }.filterNotNull()

        val visited = mutableSetOf<Point2d>()
        return galaxies.sumOf { galaxy ->
            visited.add(galaxy)
            galaxies.minus(visited).sumOf { other -> galaxy.distanceTo(other) }
        }
    }

    fun calculatePartTwo(input: List<String>, expansionFactor: Int): Long {
        val universeMap = Array(input.size) { Array(input.first().length) { ' ' } }
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                universeMap[y][x] = c
            }
        }

        val rowsToExpand = mutableSetOf<Int>()
        val columnsToExpand = mutableSetOf<Int>()
        for (y in universeMap.indices) {
            if (universeMap[y].all { it == '.' }) {
                rowsToExpand.add(y)
            }
        }
        for (x in universeMap.first().indices) {
            if (Array(universeMap.size) { universeMap[it][x] }.all { it == '.'}) {
                columnsToExpand.add(x)
            }
        }

        val galaxies = universeMap.flatMapIndexed { y, chars ->
            chars.mapIndexed { x, c ->
                if (c == '#') {
                    // well, this is easier, no?
                    Point2d(
                        x + columnsToExpand.filter { it < x }.size * (expansionFactor - 1),
                        y + rowsToExpand.filter { it < y }.size * (expansionFactor - 1)
                    )
                } else {
                    null
                }
            }
        }.filterNotNull()

        /*
        galaxies.forEach { galaxy ->
            println("Galaxy at ${galaxy.x}, ${galaxy.y}")
        }
         */

        val visited = mutableSetOf<Point2d>()
        return galaxies.sumOf { galaxy ->
            visited.add(galaxy)
            galaxies.minus(visited).sumOf { other ->
                val distance = galaxy.distanceTo(other).toLong()
                //println("Distance between ${galaxy.x},${galaxy.y} and ${other.x},${other.y} is $distance")
                distance
            }
        }

    }
}