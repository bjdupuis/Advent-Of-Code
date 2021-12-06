package days.aoc2015

import days.Day

class Day8: Day(2015, 8) {
    override fun partOne(): Any {
        return inputList.map {
            it.length - inMemorySpace(it)
        }.sum()
    }

    private fun inMemorySpace(string: String): Int {
        return encode(string).length
    }

    private fun encode(string: String): String {
        var result = Regex("\"(.*)\"").replace(string, "$1")
        result = Regex("\\\\x([a-f0-9]{2})").replace(result, "A")
        result = Regex("\\\\\"").replace(result, "\"")
        result = Regex("\\\\\\\\").replace(result, "\\\\")
        return result
    }

    override fun partTwo(): Any {
        return inputList.map {
            var result = Regex("\\\\\\\\").replace(it, "°")
            result = Regex("\\\\\"").replace(result, "‡")
            result = Regex("\"").replace(result, "€")
            result = Regex("\\\\x([a-f0-9]{2})").replace(result, "\\\\\\\\x$1")
            result = Regex("°").replace(result, "\\\\\\\\\\\\\\\\")
            result = Regex("‡").replace(result, "\\\\\\\\\\\\\"")
            result = Regex("€").replace(result, "\\\\\"")
            result = "\"" + result + "\""
            println("Original is $it, replacement is $result")
            result.length - it.length
        }.sum()
    }
}