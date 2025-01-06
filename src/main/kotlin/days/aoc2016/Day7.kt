package days.aoc2016

import days.Day

class Day7 : Day(2016, 7) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        return input.count { line ->
            val good = line.replace(Regex("\\[.*?]"), " ")
            var inside = false
            val bad = line.filter {
                if (it == '[') inside = true else if (it == ']') inside = false
                inside && it.isLetter()
            }
            good.isAbba() && !bad.isAbba()
        }
    }

    fun calculatePartTwo(input: List<String>): Int {
        return input.count { line ->
            val good = line.replace(Regex("\\[.*?]"), " ")
            var inside = false
            val bad = line.filter {
                if (it == '[') inside = true else if (it == ']') inside = false
                inside && it.isLetter()
            }
            good.getAbas().any { bad.getAbas().contains(Pair(it.second, it.first)) }
        }
    }

    private fun String.isAbba(): Boolean {
        for (i in 0 .. lastIndex - 3) {
            if (get(i) == get(i + 3) && get(i + 1) == get(i + 2) && get(i) != get(i + 1))
                return true
        }
        return false
    }

    private fun String.getAbas(): List<Pair<Char,Char>> {
        val abas = mutableListOf<Pair<Char,Char>>()
        for (i in 0 .. lastIndex - 2) {
            if (get(i) == get(i + 2) && get(i + 1) != get(i))
                abas.add(get(i) to get(i + 1))
        }
        return abas
    }
}

