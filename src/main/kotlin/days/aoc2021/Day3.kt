package days.aoc2021

import days.Day

class Day3 : Day(2021, 3) {
    override fun partOne(): Any {
        calculateGammaAndEpsilon(inputList).let { return it.first * it.second }
    }

    fun calculateGammaAndEpsilon(input: List<String>): Pair<Int,Int> {
        val counts = Array(input.first().length) { 0 }
        input.forEach { line ->
            line.forEachIndexed { index, c ->
                when (c) {
                    '0' -> counts[index]++
                }
            }
        }
        val gamma = counts.reversedArray().foldIndexed(0) { index, acc, i ->
            if (i < input.size / 2) (acc + (1 shl index)) else acc
        }
        val epsilon = counts.reversedArray().foldIndexed(0) { index, acc, i ->
            if (i > input.size / 2) (acc + (1 shl index)) else acc
        }

        return Pair(gamma, epsilon)
    }

    override fun partTwo(): Any {
        return calculateGenAndScrubberRating(inputList).let { return it.first * it.second }
    }

    fun calculateGenAndScrubberRating(input: List<String>): Pair<Int,Int> {
        var currentScrubber = input.toList()
        var currentGen = input.toList()
        input.first().indices.forEach { index ->
            currentGen = pareDownListByIndexAndValue(currentGen, index, true)
            currentScrubber = pareDownListByIndexAndValue(currentScrubber, index, false)
        }

        val gen = currentGen.first().reversed().foldIndexed(0) {index, acc, c -> if (c == '1') acc + (1 shl index) else acc}
        val scrubber = currentScrubber.first().reversed().foldIndexed(0) {index, acc, c -> if (c == '1') acc + (1 shl index) else acc}

        return Pair(gen,scrubber)
    }

    private fun pareDownListByIndexAndValue(input: List<String>, index: Int, mostCommon: Boolean): List<String> {
        if (input.size == 1) return input

        var count = 0
        input.forEach { line ->
            if (line[index] == '1') count++
        }

        val ones = count
        val zeros = input.size - count

        var filterValue = if (mostCommon) {
            if (ones >= zeros)
                '1'
            else
                '0'
        } else {
            if (zeros <= ones)
                '0'
            else
                '1'
        }
        return input.filter { it[index] == filterValue }
    }
}