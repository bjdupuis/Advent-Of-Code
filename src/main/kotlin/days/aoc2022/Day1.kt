package days.aoc2022

import days.Day

class Day1 : Day(2022, 1) {
    override fun partOne(): Any {
        return highestCalorieElf(inputList)!!
    }

    override fun partTwo(): Any {
        return highestThreeCalorieElves(inputList)!!
    }

    fun highestCalorieElf(input: List<String>): Int? {
        return parseElves(input).map { elf -> elf.sum() }.max()
    }

    fun highestThreeCalorieElves(input: List<String>): Int? {
        return parseElves(input).map { elf -> elf.sum() }.sortedDescending().take(3).sum()
    }

    private fun parseElves(input: List<String>): List<List<Int>> {
        val elves = mutableListOf<List<Int>>()

        var elf = mutableListOf<Int>()
        input.forEach { line ->
            if (line.isNullOrEmpty()) {
                elves.add(elf)
                elf = mutableListOf()
            } else {
                elf.add(line.toInt())
            }
        }
        elves.add(elf)

        return elves
    }
}