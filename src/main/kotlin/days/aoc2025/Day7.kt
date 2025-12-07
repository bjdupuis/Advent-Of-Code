package days.aoc2025

import days.Day
import util.CharArray2d
import util.Point2d

class Day7 : Day(2025, 7) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val diagram = CharArray2d(input)
        var beams = mutableSetOf<Int>()
        var splits = 0
        beams.add(diagram.getRow(0).indexOf('S'))
        for (i in 1 .. diagram.maxRowIndex) {
            val newBeams = mutableSetOf<Int>()
            beams.forEach { beam ->
                when (diagram[Point2d(beam, i)]) {
                    '^' -> {
                        splits++
                        if (beam > 0) {
                            newBeams.add(beam - 1)
                        }
                        if (beam < diagram.maxColumnIndex) {
                            newBeams.add(beam + 1)
                        }
                    }
                    '.' -> {
                        newBeams.add(beam)
                    }
                }
            }
            beams = newBeams
        }
        return splits
    }

    fun calculatePartTwo(input: List<String>): Long {
        val diagram = CharArray2d(input)
        val timelines = mutableMapOf<Int,Long>()
        timelines[diagram.getRow(0).indexOf('S')] = 1
        for (i in 1 .. diagram.maxRowIndex) {
            val keys = timelines.keys.toSet()
            for (position in keys) {
                when (diagram[Point2d(position, i)]) {
                    '^' -> {
                        timelines[position - 1] = timelines.getOrDefault(position - 1, 0) + timelines[position]!!
                        timelines[position + 1] = timelines.getOrDefault(position + 1, 0) + timelines[position]!!
                        timelines.remove(position)
                    }
                }
            }
            /*
            for (y in diagram.rowIndices) {
                for (x in diagram.columnIndices) {
                    if (y == i) {
                        print(timelines.getOrElse(x) { diagram[Point2d(x, y)] })
                    } else {
                        print(diagram[Point2d(x,y)])
                    }
                }
                println()
            }
            println("--------------------")

             */
        }


        return timelines.values.sum()
    }
}