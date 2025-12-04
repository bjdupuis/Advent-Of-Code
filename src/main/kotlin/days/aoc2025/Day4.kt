package days.aoc2025

import days.Day
import util.CharArray2d

class Day4 : Day(2025, 4) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val paperGrid = CharArray2d(input)
        var total = 0
        paperGrid.iterator().forEach { point ->
            if (paperGrid[point] == '@') {
                if (point.allNeighbors().filter { it.isWithin(paperGrid) }.map { paperGrid[it] }.count { it == '@' } < 4) {
                    total++
                }
            }
        }
        return total
    }

    fun calculatePartTwo(input: List<String>): Int {
        val paperGrid = CharArray2d(input)
        var total = 0
        var done = false
        do {
            var countedThisRound = 0
            paperGrid.iterator().forEach { point ->
                if (paperGrid[point] == '@') {
                    if (point.allNeighbors().filter { it.isWithin(paperGrid) }.map { paperGrid[it] }.count { it == '@' } < 4) {
                        paperGrid[point] = '.'
                        countedThisRound++
                    }
                }
            }
            if (countedThisRound > 0) {
                total += countedThisRound
            } else {
                done = true
            }

        } while (!done)
        return total
    }
}