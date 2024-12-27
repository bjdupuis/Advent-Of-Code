package days.aoc2024

import days.Day

class Day25 : Day(2024, 25) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val locks = input.chunked(8).filter { it.first() == "#####" }.map { lock ->
            lock.drop(1).fold(mutableListOf(0, 0, 0, 0, 0)) { lockPinHeights, row ->
                row.forEachIndexed { index, c ->
                    if (c == '#') lockPinHeights[index] = lockPinHeights[index] + 1
                }
                lockPinHeights
            }
        }
        val keys = input.chunked(8).filter { it.first() == "....." }.map { key ->
            key.dropLast(2).fold(mutableListOf(0, 0, 0, 0, 0)) { keyHeights, row ->
                row.forEachIndexed { index, c ->
                    if (c == '#') keyHeights[index] = keyHeights[index] + 1
                }
                keyHeights
            }
        }
        var matches = 0
        for (lock in locks) {
            for (key in keys) {
                if (key.fitsIn(lock)) {
                    matches++
                }
            }
        }
        return matches
    }

    fun calculatePartTwo(input: List<String>): Int {
        return 0
    }
}

private fun List<Int>.fitsIn(lock: MutableList<Int>): Boolean {
    lock.forEachIndexed { index, e -> if (lock[index] + this[index] > 5) return false }
    return true
}
