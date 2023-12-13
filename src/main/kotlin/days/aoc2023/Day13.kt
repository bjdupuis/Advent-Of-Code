package days.aoc2023

import days.Day
import util.CharArray2d
import util.Point2d
import util.downUntil
import kotlin.math.min

class Day13 : Day(2023, 13) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        return input.fold(mutableListOf<MutableList<String>>(mutableListOf())) { lists, current ->
            if (current.isEmpty()) {
                lists.add(mutableListOf())
            } else {
                lists.last().add(current)
            }
            lists
        }.filter {
            it.isNotEmpty()
        }.map { desertMapList ->
            CharArray2d(desertMapList)
        }.sumOf {
            (findMirror(it.height, it::getRow) ?: 0) * 100 + (findMirror(it.width, it::getColumn)
                ?: 0)
        }
    }

    private fun findMirror(
        size: Int,
        accessor: (Int) -> CharArray,
        ignoreAnswer: Int? = null
    ): Int? {
        outer@ for (index in 0 until size - 1) {
            if (ignoreAnswer != null) {
                if (index + 1 == ignoreAnswer) {
                    continue
                }
            }
            val numToCompare = min(min(index + 1, (size - index) - 1), size / 2)
            for (delta in index downUntil index - numToCompare) {
                val other = index + (index - delta) + 1
                if (!accessor.invoke(delta).contentEquals(accessor.invoke(other))) {
                    continue@outer
                }
            }

            return index + 1
        }

        return null
    }

    fun calculatePartTwo(input: List<String>): Int {
        return input.fold(mutableListOf<MutableList<String>>(mutableListOf())) { lists, current ->
            if (current.isEmpty()) {
                lists.add(mutableListOf())
            } else {
                lists.last().add(current)
            }
            lists
        }.filter {
            it.isNotEmpty()
        }.map { desertMapList ->
            CharArray2d(desertMapList)
        }.sumOf { desertMap ->
            val avoidRow = findMirror(desertMap.height, desertMap::getRow)
            val avoidColumn = findMirror(desertMap.width, desertMap::getColumn)

            var newAnswer = -1
            outer@ for (y in 0 until desertMap.height) {
                for (x in 0 until desertMap.width) {
                    val newMap = desertMap.clone() as CharArray2d
                    val point = Point2d(x, y)
                    newMap[point] = if (newMap[point] == '.') '#' else '.'

                    newAnswer =
                        findMirror(newMap.height, newMap::getRow, avoidRow)?.let { it * 100 }
                            ?: findMirror(newMap.width, newMap::getColumn, avoidColumn) ?: -1
                    if (newAnswer >= 0) {
                        break@outer
                    }
                }
            }

            if (newAnswer == -1) {
                throw IllegalStateException("No new answer found")
            }
            newAnswer
        }
    }
}