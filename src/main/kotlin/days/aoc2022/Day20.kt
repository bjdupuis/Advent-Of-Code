package days.aoc2022

import days.Day

class Day20 : Day(2022, 20) {
    override fun partOne(): Any {
        return calculateGroveCoordinateSum(inputList)
    }

    override fun partTwo(): Any {
        return calculateGroveCoordinateSumWithDecryptionKey(inputList)
    }

    fun calculateGroveCoordinateSumWithDecryptionKey(input: List<String>): Long {
        var numbers = parseInput(input, 811589153L)
        repeat(10) {
            numbers = mix(numbers)
        }
        val zero = numbers.indexOf(numbers.first { it.second == 0L })

        return numbers[(1000 + zero) % numbers.size].second +
                numbers[(2000 + zero) % numbers.size].second +
                numbers[(3000 + zero) % numbers.size].second
    }

    fun calculateGroveCoordinateSum(input: List<String>): Int {
        var numbers = parseInput(input, 1L)
        numbers = mix(numbers)
        val zero = numbers.indexOf(numbers.first { it.second == 0L })

        return numbers[(1000 + zero) % numbers.size].second.toInt() +
                numbers[(2000 + zero) % numbers.size].second.toInt() +
                numbers[(3000 + zero) % numbers.size].second.toInt()
    }

    private fun parseInput(input: List<String>, decryptionKey: Long): MutableList<Pair<Int, Long>> {
        return input.mapIndexed { index, string -> Pair(index, string.toLong() * decryptionKey) }.toMutableList()
    }

    private fun mix(numbers: MutableList<Pair<Int, Long>>): MutableList<Pair<Int, Long>> {
        for (i in numbers.indices) {
            val index = numbers.indexOf(numbers.first { i == it.first })
            val number = numbers.removeAt(index)
            numbers.add((number.second + index).mod(numbers.size), number)
        }

        return numbers
    }
}