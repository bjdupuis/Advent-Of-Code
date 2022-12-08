package days.aoc2022

import days.Day

class Day8 : Day(2022, 8) {
    override fun partOne(): Any {
        return calculateVisibleTrees(inputList)
    }

    override fun partTwo(): Any {
        return calculateHighestScenicScore(inputList)
    }

    fun calculateVisibleTrees(input: List<String>): Int {
        val forest = readForest(input)
        var visible = (forest.size*forest.size) - (forest.size - 2) * (forest.size - 2)
        for (y in 1 until forest.size - 1) {
            for (x in 1 until forest[y].size - 1) {
                if (checkVisibilityInDirection(forest, x, y, -1, 0)) visible++
                else if (checkVisibilityInDirection(forest, x, y, 1, 0)) visible++
                else if (checkVisibilityInDirection(forest, x, y, 0, 1)) visible++
                else if (checkVisibilityInDirection(forest, x, y, 0, -1)) visible++
            }
        }
        return visible
    }

    private fun checkVisibilityInDirection(forest: Array<Array<Int>>, x: Int, y: Int, xDelta: Int, yDelta: Int): Boolean {
        val height = forest[x][y]
        if (xDelta != 0) {
            var i = x + xDelta
            while (i in forest.indices) {
                if (height <= forest[i][y]) return false
                i += xDelta
            }
        } else {
            var i = y + yDelta
            while (i in forest.indices) {
                if (height <= forest[x][i]) return false
                i += yDelta
            }
        }

        return true
    }

    fun calculateHighestScenicScore(input: List<String>): Int {
        val forest = readForest(input)
        var highestScenicScore = 0
        for (y in 1 until forest.size - 1) {
            for (x in 1 until forest[y].size - 1) {
                val score =
                    calculateSightDistanceForDirection(forest, x, y, 1, 0) *
                    calculateSightDistanceForDirection(forest, x, y, -1, 0) *
                    calculateSightDistanceForDirection(forest, x, y, 0, 1) *
                    calculateSightDistanceForDirection(forest, x, y, 0, -1)

                if (score > highestScenicScore) highestScenicScore = score
            }
        }

        return highestScenicScore
    }

    private fun calculateSightDistanceForDirection(forest: Array<Array<Int>>, x: Int, y: Int, xDelta: Int, yDelta: Int): Int {
        val height = forest[x][y]
        var sightDistance = 0
        if (xDelta != 0) {
            var i = x + xDelta
            while (i in forest.indices) {
                if (height <= forest[i][y]) return sightDistance + 1
                i += xDelta
                sightDistance++
            }
        } else {
            var i = y + yDelta
            while (i in forest.indices) {
                if (height <= forest[x][i]) return sightDistance + 1
                i += yDelta
                sightDistance++
            }
        }

        return sightDistance
    }

    private fun readForest(input: List<String>): Array<Array<Int>> {
        val forest = Array(input.size) {
            Array(input.first().length) { 0 }
        }

        var x = 0
        var y = 0
        input.forEach { line ->
            line.forEach { c ->
                forest[x][y] = c.toString().toInt()
                x++
            }
            y++
            x = 0
        }

        return forest
    }
}