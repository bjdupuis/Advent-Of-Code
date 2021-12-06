package days.aoc2015

import days.Day

class Day5: Day(2015, 5) {
    override fun partOne(): Any {
        return inputList.filter { validateString(it) }.count()
    }

    fun validateString(string: String): Boolean {
        val naughtyStrings = setOf("ab", "cd", "pq", "xy")
        val vowels = "aeiou"

        return string.filter { it in vowels }.length >= 3
                && naughtyStrings.none { string.contains(it) }
                && Regex("(.)\\1").containsMatchIn(string)
    }

    override fun partTwo(): Any {
        return inputList.filter {
            Regex("(.)(.).*\\1\\2").containsMatchIn(it)
                    && Regex("(.).\\1").containsMatchIn(it)
        }.count()
    }
}