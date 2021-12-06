package days.aoc2015

import days.Day

class Day19: Day(2015, 19) {
    override fun partOne(): Any {
        val replacements = inputList.takeWhile { it.isNotBlank() }
        val target = inputList.last()

        return calculatePartOne(replacements, target)
    }

    override fun partTwo(): Any {
        return false
    }

    fun calculatePartOne(replacements: List<String>, molecule: String): Int {
        val distinctMolecules = mutableSetOf<String>()

        replacements.forEach { replacement ->
            val (searchFor, replaceWith) = replacement.trim().split(" => ")
            Regex(searchFor).findAll(molecule).forEach {
                val newOne = StringBuilder(molecule.substring(0, it.range.first))
                newOne.append(replaceWith)
                newOne.append(molecule.substring(it.range.first + searchFor.length))

                println("$searchFor => $replaceWith:  $newOne")
                distinctMolecules.add(newOne.toString())
            }
        }

        return distinctMolecules.size
    }

    fun calculatePartTwo(replacements: List<String>, molecule: String): Int {
        return 0
    }
}