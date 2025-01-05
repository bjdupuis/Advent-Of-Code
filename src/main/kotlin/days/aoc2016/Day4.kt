package days.aoc2016

import days.Day

class Day4 : Day(2016, 4) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        return input.sumOf(this::sectorIdIfValid)
    }

    private fun sectorIdIfValid(room: String): Int {
        val groupedLetters = room
            .takeWhile { !it.isDigit() }
            .filterNot { it == '-' }
            .groupBy { it }.values
            .sortedBy { it.size }
            .reversed()
            .groupBy { it.size }
            .flatMap { it.value.sortedBy { it.first() } }
            .take(5)
            .map { it.first() }
            .joinToString("")

        return if (room.dropWhile { it != '[' }.drop(1).takeWhile { it != ']' } == groupedLetters) room.filter { it.isDigit() }.toInt() else 0
    }

    fun calculatePartTwo(input: List<String>): Int {
        input.forEach { line ->
            if (sectorIdIfValid(line) != 0) {
                val rotation = line.filter { it.isDigit() }.toInt()
                val unencrypted = line.map { if (it == '-') ' ' else 'a' + (((it - 'a') + rotation) % 26) }.joinToString("")
                if (unencrypted.startsWith("northpole object storage")) return rotation
            }
        }
        return 0
    }
}