package days.aoc2016

import days.Day

class Day6 : Day(2016, 6) {
    override fun partOne(): String {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): String {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): String {
        val columnMap = mutableMapOf<Int,MutableMap<Char,Int>>()
        input.forEach { line ->
            line.forEachIndexed { index, c ->
                columnMap.getOrPut(index) { mutableMapOf() }.let {
                    it[c] = it.getOrDefault(c, 0) + 1
                }
            }
        }

        return columnMap.map { it.value.maxBy { it.value }.key }.joinToString("")
    }

    fun calculatePartTwo(input: List<String>): String {
        val columnMap = mutableMapOf<Int,MutableMap<Char,Int>>()
        input.forEach { line ->
            line.forEachIndexed { index, c ->
                columnMap.getOrPut(index) { mutableMapOf() }.let {
                    it[c] = it.getOrDefault(c, 0) + 1
                }
            }
        }

        return columnMap.map { it.value.minBy { it.value }.key }.joinToString("")
    }
}