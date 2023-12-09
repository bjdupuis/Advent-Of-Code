package days.aoc2023

import days.Day

class Day9 : Day(2023, 9) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Long {
        return input.sumOf { calculateAdditionalValueOf(it) }
    }

    fun calculatePartTwo(input: List<String>): Long {
        return input.sumOf { calculateAdditionalValueOf(it, true) }
    }

    private fun calculateAdditionalValueOf(valueString: String, reversed: Boolean = false): Long {
        var toReduce = valueString.split(' ').map { it.toLong() }
        val intermediates = mutableListOf(toReduce)
        while (toReduce.any { it != 0L }) {
            toReduce = toReduce.zipWithNext { a, b -> b - a }
            intermediates.add(toReduce)
        }

        val additions = mutableListOf<List<Long>>()
        var valueToInclude = 0L
        intermediates.reversed().forEach {
            val replacement = if (!reversed)
                it.plus(valueToInclude + it.last())
            else
                listOf(it.first() - valueToInclude).plus(it)

            valueToInclude = if (!reversed) replacement.last() else replacement.first()
            additions.add(replacement)
        }

        return if (!reversed) additions.last().last() else additions.last().first()
    }
}