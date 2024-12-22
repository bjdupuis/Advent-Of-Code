package days.aoc2024

import days.Day
import kotlinx.coroutines.yield
import kotlin.math.max

class Day22 : Day(2024, 22) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Long {
        return input.sumOf { line ->
            var secretNumber = line.toLong()
            repeat(2000) {
                secretNumber = calculateNextSecretNumber(secretNumber)
            }
            secretNumber
        }
    }

    private fun calculateNextSecretNumber(secretNumber: Long): Long {
        var value = secretNumber.mixAndPrune(secretNumber * 64)
        value = value.mixAndPrune(value / 32)
        value = value.mixAndPrune(value * 2048)
        return value
    }

    fun calculatePartTwo(input: List<String>): Int {
        val monkeyPriceDeltas = input.map { deltas(it.toLong()).take(2000).windowed(4).map {
            Pair(it.map { it.second }, it.last().first)
        }.toMutableList() }
        var max = 0
        for (i in monkeyPriceDeltas.indices) {
            println("Processing $i")
            val monkeyPriceDelta = monkeyPriceDeltas[i]
            if (monkeyPriceDelta.isEmpty()) continue
            max = max(max, monkeyPriceDelta.maxOf { window ->
                var total = window.second
                outer@ for (j in i + 1 .. monkeyPriceDeltas.lastIndex) {
                    val other = monkeyPriceDeltas[j]
                    for (otherWindow in other) {
                        if (otherWindow.first == window.first) {
                            total += otherWindow.second
                            other.remove(otherWindow)
                            continue@outer
                        }
                    }
                }
                total
            })
        }
        return max
    }

    private fun deltas(initial: Long): Sequence<Pair<Int,Int>> = sequence {
        var previous = initial % 10
        var value = initial
        while(true) {
            value = calculateNextSecretNumber(value)
            val ones = (value % 10).toInt()
            yield(Pair(ones, ((value % 10) - previous).toInt() ))
            previous = value % 10
        }
    }
}

private fun Long.mixAndPrune(other: Long): Long {
    return (this xor other) % 16777216L
}

