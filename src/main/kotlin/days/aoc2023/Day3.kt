package days.aoc2023

import days.Day
import util.Point2d
import java.awt.Point

class Day3 : Day(2023, 3) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    fun calculatePartOne(inputList: List<String>): Int {
        var sum = 0
        var currentNumber = StringBuilder()
        var isPartNumber = false

        val processNumber = {
            if (currentNumber.isNotBlank()) {
                if (isPartNumber) {
                    sum += currentNumber.toString().toInt()
                }
                currentNumber.clear()
                isPartNumber = false
            }
        }

        inputList.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    in '0'..'9' -> {
                        currentNumber.append(c)
                        Point2d(x, y).allNeighbors().filter { it.isWithin(inputList) }.forEach {
                            val currentChar = inputList[it.y][it.x]
                            if (!currentChar.isDigit() && currentChar != '.') {
                                isPartNumber = true
                            }
                        }
                    }

                    else -> processNumber.invoke()
                }
            }
            processNumber.invoke()
        }
        return sum
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartTwo(inputList: List<String>): Int {
        var sum = 0

        inputList.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (!c.isDigit() && c != '.') {
                    val potentialGear = Point2d(x, y)
                    val digitNeighbors = potentialGear.allNeighbors()
                        .filter { it.isWithin(inputList) && inputList[it.y][it.x].isDigit() }
                    val adjacentPartNumbers = adjacentPartNumberPositions(potentialGear, digitNeighbors)
                    if (adjacentPartNumbers.count() == 2) {
                        sum += adjacentPartNumbers.fold(1) { acc, neighbor -> acc * partNumberFromPosition(neighbor, inputList) }
                    }
                }
            }
        }

        return sum
    }

    private fun partNumberFromPosition(point: Point2d, inputList: List<String>): Int {
        // we just know that we're somewhere in a number. Find the beginning
        var beginning = point
        var done = false
        do {
            val before = beginning.copy(x = beginning.x - 1)
            if (before.isWithin(inputList) && inputList[before.y][before.x].isDigit()) {
                beginning = before
            } else {
                done = true
            }
        } while (!done)

        val currentNumber = StringBuilder()
        var current = beginning
        while(current.isWithin(inputList) && inputList[current.y][current.x].isDigit()) {
            currentNumber.append(inputList[current.y][current.x])
            current = current.copy(x = current.x + 1)
        }
        return currentNumber.toString().toInt()
    }

    private fun adjacentPartNumberPositions(point: Point2d, pointsWithDigits: List<Point2d>): List<Point2d> {
        val distinct = { points: List<Point2d> ->
            // check for the case where there are two part numbers, meaning the center element
            // is not a digit. If the center position is a digit then it is part of a single part
            // number.
            if (points.count() == 2) {
                if (points.map { it.x }.contains(point.x)) {
                    listOf(points.first())
                } else {
                    points
                }
            } else if (points.isNotEmpty()){
                listOf(points.first())
            } else {
                emptyList()
            }
        }
        val above = pointsWithDigits.filter { it.y == point.y - 1 }
        val below = pointsWithDigits.filter { it.y == point.y + 1 }
        val beside = pointsWithDigits.filter { it.y == point.y }

        return beside + distinct(above) + distinct(below)
    }
}
