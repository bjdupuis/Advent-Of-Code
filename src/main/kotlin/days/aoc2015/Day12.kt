package days.aoc2015

import days.Day

class Day12: Day(2015, 12) {
    override fun partOne(): Any {
        return calculateSumOfNumbers(inputString)
    }

    override fun partTwo(): Any {
        return calculateNonRedNumbersInObject(inputString, 0).first
    }

    fun calculateSumOfNumbers(string: String): Int {
        return Regex("(-?)(\\d+)").findAll(string).map {
            it.destructured.let {
                (sign, number) ->
                if ("-" == sign)
                    number.toInt().unaryMinus()
                else
                    number.toInt()
            }
        }.sum()
    }

    fun calculateNonRedNumbersInObject(string: String, offset: Int): Pair<Int, Int> {

        var total = 0
        var token = ""
        var inKey = true
        var containsRed = false
        var i = offset
        var sign = '+'
        while (i < string.length) {

            when(string[i]) {
                '{' -> {
                    val (inObject, end) = calculateNonRedNumbersInObject(string, i+1)
                    total += inObject
                    i = end
                }

                '}' -> {
                    if (Regex("\\d+").matches(token)) {
                        val value = token.toInt()
                        total += if (sign == '-') value.unaryMinus() else value
                    }
                    return Pair(if (containsRed) 0 else total, i)
                }

                '[' -> {
                    val (inArray, end) = calculateNonRedNumbersInArray(string, i+1)
                    total += inArray
                    i = end
                }

                '"' -> {
                    if (!inKey) {
                        if ("red" == token) {
                            containsRed = true
                        }
                    }
                }

                '-' -> {
                    sign = '-'
                }

                ':' -> {
                    inKey = false
                    token = ""
                }

                ',' -> {
                    if (Regex("\\d+").matches(token)) {
                        val value = token.toInt()
                        total += if (sign == '-') value.unaryMinus() else value
                        sign = '+'
                        token = ""
                    }

                    inKey = true
                }

                in ('a'..'z'), in ('0'..'9') -> {
                    token += string[i]
                }
            }

            i++
        }

        return Pair(total, -1)
    }

    private fun calculateNonRedNumbersInArray(string: String, offset: Int): Pair<Int, Int> {
        var total = 0
        var token = ""
        var i = offset
        var sign = '+'
        while (i < string.length) {

            when(string[i]) {
                '{' -> {
                    val (inObject, end) = calculateNonRedNumbersInObject(string, i+1)
                    total += inObject
                    i = end
                }

                '[' -> {
                    val (inArray, end) = calculateNonRedNumbersInArray(string, i+1)
                    total += inArray
                    i = end
                }

                ']' -> {
                    if (token.isNotBlank()) {
                        val value = token.toInt()
                        total += if (sign == '-') value.unaryMinus() else value
                    }

                    return Pair(total, i)
                }

                ',' -> {
                    if (token.isNotBlank()) {
                        val value = token.toInt()
                        total += if (sign == '-') value.unaryMinus() else value
                        sign = '+'
                        token = ""
                    }
                }

                '-' -> {
                    sign = '-'
                }


                in ('0'..'9') -> {
                    token += string[i]
                }
            }

            i++
        }

        return Pair(total, -1)

    }

}