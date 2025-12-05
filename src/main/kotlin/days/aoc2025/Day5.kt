package days.aoc2025

import days.Day
import util.intersects

class Day5 : Day(2025, 5) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val freshList = input.takeWhile { it.isNotEmpty() }.map { line ->
            var range: LongRange
            Regex("(\\d+)-(\\d+)").matchEntire(line)!!.destructured.let { (low, high) ->
                range = low.toLong() .. high.toLong()
            }
            range
        }
        val ingredients = input.dropWhile { it.isNotEmpty() }.drop(1)
        var total = 0
        ingredients.forEach { ingredient ->
            val ingredientNumber = ingredient.toLong()
            for (i in freshList) {
                if (i.contains(ingredientNumber)) {
                    total++
                    break
                }
            }
        }
        return total
    }

    fun calculatePartTwo(input: List<String>): Long {
        val freshList = input.takeWhile { it.isNotEmpty() }.map { line ->
            var range: LongRange
            Regex("(\\d+)-(\\d+)").matchEntire(line)!!.destructured.let { (low, high) ->
                range = low.toLong() .. high.toLong()
            }
            range
        }.toSet()

        val ranges = coalesceRanges(freshList)
        return ranges.sumOf { (it.last() - it.first()) + 1L }
    }
}

private fun coalesceRanges(input: Set<LongRange>): MutableSet<LongRange> {
    val distinctRanges = mutableSetOf<LongRange>()
    input.forEach { range ->
        val rangesToRemove = mutableSetOf<LongRange>()
        var rangesToAdd = mutableSetOf<LongRange>()
        if (distinctRanges.isEmpty()) {
            rangesToAdd.add(range)
        } else {
            for (distinct in distinctRanges) {
                if (distinct.intersects(range)) {
                    rangesToRemove.add(distinct)
                    rangesToAdd.add(distinct.first().coerceAtMost(range.first())..distinct.last()
                        .coerceAtLeast(range.last())
                    )
                }
            }
            if (rangesToAdd.isEmpty()) {
                rangesToAdd.add(range)
            } else if (rangesToAdd.size > 1) {
                rangesToAdd = coalesceRanges(rangesToAdd)
            }
        }
        rangesToRemove.forEach { distinctRanges.remove(it) }
        rangesToAdd.forEach { distinctRanges.add(it) }
    }
    return if (distinctRanges.size == input.size) {
        distinctRanges
    } else {
        coalesceRanges(distinctRanges)
    }
}
