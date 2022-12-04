package days.aoc2022

import days.Day

class Day4 : Day(2022, 4) {
    override fun partOne(): Any {
        return countFullyContainedRanges(inputList)
    }

    override fun partTwo(): Any {
        return countIntersectingRanges(inputList)
    }

    fun countFullyContainedRanges(input: List<String>): Int {
        return parseAndApplyCheckerToSets(input) { set1, set2, intersection ->
            (intersection == set1) or (intersection == set2)
        }
    }

    fun countIntersectingRanges(input: List<String>): Int {
        return parseAndApplyCheckerToSets(input) { _, _, intersection ->
            intersection.isNotEmpty()
        }
    }

    private fun parseAndApplyCheckerToSets(input: List<String>, checker: (Set<Int>, Set<Int>, Set<Int>) -> Boolean): Int {
        return input.map { line ->
            val (low1, high1, low2, high2) = Regex("(\\d+)-(\\d+),(\\d+)-(\\d+)").matchEntire(line)!!.destructured
            Pair((low1.toInt()..high1.toInt()).toSet(), (low2.toInt()..high2.toInt()).toSet())
        }.count {
            checker.invoke(it.first, it.second, it.first.intersect(it.second))
        }
    }
}