package days.aoc2022

import days.Day
import kotlin.math.abs

class Day25 : Day(2022, 25) {
    override fun partOne(): Any {
        return calculateSumInSnafu(inputList)
    }

    override fun partTwo(): Any {
        return 0
    }

    fun calculateSumInSnafu(input: List<String>): String {
        val sum = input.sumOf { Snafu(it).toLong() }
        return sum.toSnafu().value
    }

}

class Snafu(val value: String) {
    fun toLong(): Long {
        var multiplier = 1L
        var total = 0L
        for (i in value.lastIndex downTo 0) {
            when (value[i]) {
                '2' -> total += 2 * multiplier
                '1' -> total += multiplier
                '-' -> total += multiplier * -1
                '=' -> total += multiplier * -2
            }

            multiplier *= 5L
        }

        return total
    }
}

fun Long.toSnafu(): Snafu {
    var multiplicand = 1L
    var maxForDigit = 0L
    while (maxForDigit + multiplicand * 2 < this) {
        maxForDigit += multiplicand * 2
        multiplicand *= 5
    }

    val sb = StringBuilder()
    var remainder = this
    while (multiplicand >= 1) {
        if (remainder > 0) {
            if (remainder > multiplicand + maxForDigit) {
                sb.append('2')
                remainder -= 2 * multiplicand
            } else if (remainder > maxForDigit) {
                sb.append('1')
                remainder -= multiplicand
            } else {
                sb.append('0')
            }
        } else if (remainder < 0) {
            if (abs(remainder) > multiplicand + maxForDigit) {
                sb.append('=')
                remainder += 2 * multiplicand
            } else if (abs(remainder) > maxForDigit) {
                sb.append('-')
                remainder += multiplicand
            } else {
                sb.append('0')
            }
        } else {
            sb.append('0')
        }

        multiplicand /= 5
        maxForDigit -= multiplicand * 2
    }

    return Snafu(sb.toString())
}
