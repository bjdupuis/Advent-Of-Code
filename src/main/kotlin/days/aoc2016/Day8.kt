package days.aoc2016

import days.Day
import util.CharArray2d
import util.Point2d

class Day8 : Day(2016, 8) {
    override fun partOne(): Any {
        return calculatePartOne(inputList, Point2d(50,6))
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>, size: Point2d): Int {
        val display = CharArray2d(size.x, size.y, '.')

        input.forEach { instruction ->
            if (instruction.startsWith("rect")) {
                Regex("rect (\\d+)x(\\d+)").matchEntire(instruction)?.destructured?.let { (width, height) ->
                    for (x in 0 until width.toInt()) {
                        for (y in 0 until height.toInt()) {
                            display[Point2d(x,y)] = '#'
                        }
                    }
                }
            } else if (instruction.startsWith("rotate column x")) {
                Regex("rotate column x=(\\d+) by (\\d+)").matchEntire(instruction)?.destructured?.let { (column, delta) ->
                    repeat (delta.toInt()) {
                        val temp = display[Point2d(column.toInt(), display.maxRowIndex)]
                        for (y in display.maxRowIndex downTo 0) {
                            if (y == 0) {
                                display[Point2d(column.toInt(), y)] = temp
                            } else {
                                display[Point2d(column.toInt(), y)] = display[Point2d(column.toInt(), y - 1)]
                            }
                        }
                    }
                }
            } else if (instruction.startsWith("rotate row y")) {
                Regex("rotate row y=(\\d+) by (\\d+)").matchEntire(instruction)?.destructured?.let { (row, delta) ->
                    repeat(delta.toInt()) {
                        val temp = display[Point2d(display.maxColumnIndex, row.toInt())]
                        for (x in display.maxColumnIndex downTo 0) {
                            if (x == 0) {
                                display[Point2d(x, row.toInt())] = temp
                            } else {
                                display[Point2d(x, row.toInt())] = display[Point2d(x - 1, row.toInt())]
                            }
                        }
                    }
                }
            }
        }

        display.print()

        var count = 0
        for (y in display.rowIndices) {
            for (x in display.columnIndices) {
                if (display[Point2d(x,y)] == '#')
                    count++
            }
        }

        return count
    }

    fun calculatePartTwo(input: List<String>): Int {
        return 0
    }
}