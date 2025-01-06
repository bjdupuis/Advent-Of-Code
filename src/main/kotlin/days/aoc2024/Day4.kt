package days.aoc2024

import days.Day
import util.CharArray2d
import util.Point2d

class Day4 : Day(2024, 4) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val array = CharArray2d(input)
        var sum = 0
        for (y in array.rowIndices) {
            for (x in array.columnIndices) {
                sum += countXmasFrom(array, Point2d(x, y))
            }
        }
        return sum
    }

    private fun countXmasFrom(array: CharArray2d, point: Point2d, previous: Point2d? = null, index: Int = 0): Int {
        val xmas = arrayOf('X', 'M', 'A', 'S')

        if (array[point] == xmas[index]) {
            if (index == 3) {
                return 1
            }
            return if (previous != null) {
                val next = Point2d(point.x + (point.x - previous.x), point.y + (point.y - previous.y))
                if (next.isWithin(array)) {
                    countXmasFrom(array, next, point, index + 1)
                } else {
                    0
                }
            } else {
                point.allNeighbors().filter { it.isWithin(array) }
                    .sumOf { neighbor ->
                        countXmasFrom(array, neighbor, point, index + 1)
                    }
            }
        }

        return 0
    }

    fun calculatePartTwo(input: List<String>): Int {
        val array = CharArray2d(input)
        var sum = 0
        for (y in 1 until array.maxColumnIndex) {
            for (x in 1 until array.maxRowIndex) {
                sum += countMasFrom(array, Point2d(x, y))
            }
        }
        return sum
    }

    private fun countMasFrom(array: CharArray2d, point: Point2d): Int {
        if (array[point] == 'A') {
            val upperRight = Point2d(point.x + 1, point.y - 1)
            val upperLeft = Point2d(point.x - 1, point.y - 1)
            val lowerRight = Point2d(point.x + 1, point.y + 1)
            val lowerLeft = Point2d(point.x - 1, point.y + 1)

            if ((array[upperRight] == 'M' && array[lowerLeft] == 'S' ||
                array[upperRight] == 'S' && array[lowerLeft] == 'M') &&
                (array[lowerRight] == 'M' && array[upperLeft] == 'S' ||
                array[lowerRight] == 'S' && array[upperLeft] == 'M')) {
                return 1
            }
        }

        return 0
    }

}