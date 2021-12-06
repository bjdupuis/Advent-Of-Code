package days.aoc2021

import days.Day

class Day1 : Day(2021, 1) {
    override fun partOne(): Any {
        return countIncreasingPairs(inputList.map { it.toInt() })
    }

    fun countIncreasingPairs(list: List<Int>): Int {
        return pairs(list)
            .filter { pair -> pair.first < pair.second }
            .count()
    }

    override fun partTwo(): Any {
        return countIncreasingTriples(inputList.map { it.toInt() })
    }

    fun countIncreasingTriples(list: List<Int>): Int {
        val sums = triples(list)
            .map { it.first + it.second + it.third }
        return countIncreasingPairs(sums.toList())
    }

    private fun pairs(list: List<Int>): Sequence<Pair<Int,Int>> = sequence {
        for (i in list.indices)
            if (i < list.size - 1)
                yield(Pair(list[i], list[i+1]))
    }

    private fun triples(list: List<Int>): Sequence<Triple<Int,Int,Int>> = sequence {
        for (i in list.indices)
            if (i < list.size - 2)
                yield(Triple(list[i], list[i+1], list[i+2]))
    }

}