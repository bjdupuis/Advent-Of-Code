package days.aoc2015

import days.Day

class Day25 : Day(2015, 25) {
    override fun partOne(): Any {
        return calculatePartOne(inputString)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: String): Long {
        val (row, column) = input.split(",").map(String::toInt)
        return codeForRowAndColumn(row, column)
    }

    private fun codeForRowAndColumn(row: Int, column: Int): Long {
        return (1 until indexOfRowAndColumn(row, column)).fold(20151125L) { acc, _ ->
            (acc * 252533L).rem(33554393)
        }
    }

    private fun indexOfRowAndColumn(row: Int, column: Int): Int {
        var index = 1
        var currentRow = 1
        while (currentRow < row + (column - 1)) {
            index += currentRow++
        }
        return index + (column - 1)
    }

    fun calculatePartTwo(input: List<String>): Int {
        return 0
    }
}