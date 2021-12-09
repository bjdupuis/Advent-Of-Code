package days.aoc2021

import days.Day
import kotlin.math.exp

class Day9 : Day(2021, 9) {
    override fun partOne(): Any {
        return calculateRiskLevelSum(inputList)
    }

    override fun partTwo(): Any {
        return calculateThreeLargestBasins(inputList)
    }

    fun calculateThreeLargestBasins(inputList: List<String>): Int {
        val seafloor = Seafloor(inputList)
        val basinSizes = mutableListOf<Int>()
        for (x in inputList.indices) {
            for (y in inputList[x].indices) {
                seafloor.exploreBasin(x, y).let {
                    if (it > 0) {
                        basinSizes.add(it)
                    }
                }
            }
        }

        return basinSizes.sortedDescending().take(3).reduce { acc, i -> acc * i }
    }

    class Seafloor(inputList: List<String>) {
        private var floor: List<List<Point>> = inputList.map { line ->
            line.trim().map { char -> char.toString().toInt() }.map { Point(it, false) }
        }

        // recursively explore this basin and return a running size
        fun exploreBasin(x: Int, y: Int): Int {
            floor.getOrElse(x) { listOf() }.getOrNull(y)?.let {
                if (!(it.height == 9 || it.visited)) {
                    it.visited = true
                    return 1 + exploreBasin(x - 1, y) +
                            exploreBasin(x + 1, y) +
                            exploreBasin(x, y - 1) +
                            exploreBasin(x, y + 1)
                }
            }

            return 0
        }

        class Point(val height: Int, var visited: Boolean)
    }

    fun calculateRiskLevelSum(inputLines: List<String>): Int {
        var sum = 0
        inputLines.map { line -> line.trim().map { char -> char.toString().toInt() } }.let { seafloor ->
            for (x in seafloor.indices) {
                val row = seafloor[x]
                for (y in row.indices) {
                    sum += if (seafloor[x][y] < seafloor.getOrElse(x - 1) { listOf() }.getOrElse(y) {9}  &&
                        seafloor[x][y] < seafloor.getOrElse(x + 1) { listOf() }.getOrElse(y) {9}  &&
                        seafloor[x][y] < seafloor.getOrElse(x) { listOf() }.getOrElse(y - 1) {9}  &&
                        seafloor[x][y] < seafloor.getOrElse(x) { listOf() }.getOrElse(y + 1) {9}) {
                        seafloor[x][y] + 1
                    } else {
                        0
                    }
                }
            }
        }

        return sum
    }
}