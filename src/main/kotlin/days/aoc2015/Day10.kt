package days.aoc2015

import days.Day

class Day10: Day(2015, 10) {
    override fun partOne(): Any {
        var phrase = "3113322113"
        for (i in 0 until 40) {
            phrase = calculateLookAndSay(phrase)
        }

        return phrase.length
    }

    fun calculateLookAndSay(phrase: String): String {
        return Regex("(\\d)(\\1*)").findAll(phrase).map {
                it.destructured.let { (number, additional) ->
                    "${additional.length+1}$number"
                }
            }.joinToString(separator = "")
    }

    override fun partTwo(): Any {
        var phrase = "3113322113"
        for (i in 0 until 50) {
            phrase = calculateLookAndSay(phrase)
        }

        return phrase.length
    }
}