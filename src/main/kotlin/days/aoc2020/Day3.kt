package days.aoc2020

import days.Day

class Day3 : Day(2020, 3) {
    override fun partOne(): Any {
        return performCount(3, 1)
    }

    override fun partTwo(): Any {
        return performCount(1,1) *
                performCount(3, 1) *
                performCount(5, 1) *
                performCount(7, 1) *
                performCount(1, 2)
    }

    fun performCount(xOffset : Int, yOffset : Int): Long {
        var xIndex = 0
        var treeCount = 0
        for (yIndex in yOffset until inputList.size step yOffset) {
            val line = inputList[yIndex]
            xIndex += xOffset
            if (xIndex !in line.indices) {
                xIndex -= line.length
            }

            if (line[xIndex] == '#') {
                treeCount++
            }
        }

        return treeCount.toLong()
    }
}