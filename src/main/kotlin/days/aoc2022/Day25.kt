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
    var divisor = 1L
    var total = 0L
    while (total + divisor * 2 < this) {
        total += divisor * 2
        divisor *= 5
    }

    val sb = StringBuilder()
    var remainder = this
    while (divisor >= 1) {
        if (remainder > 0) {
            if (remainder > divisor + total) {
                sb.append('2')
                remainder -= 2 * divisor
            } else if (remainder > total) {
                sb.append('1')
                remainder -= divisor
            } else {
                sb.append('0')
            }
        } else if (remainder < 0) {
            if (abs(remainder) > divisor + total) {
                sb.append('=')
                remainder += 2 * divisor
            } else if (abs(remainder) > total) {
                sb.append('-')
                remainder += divisor
            } else {
                sb.append('0')
            }
        } else {
            sb.append('0')
        }

        divisor /= 5
        total -= divisor * 2
    }

    return Snafu(sb.toString())
}
