package days.aoc2020

import days.Day

class Day15: Day(2020, 15) {
    override fun partOne(): Any {
        return calculateFinalNumber(2020)
    }

    fun calculateFinalNumber(end: Int): Int {
        val turnMap = HashMap<Int,Int>()
        val inputNumbers = inputString.split(",").map { it.toInt() }
        var nextToSay = 0
        var sayingNow = 0

        for (i in 1..end) {
            sayingNow = when {
                i - 1 in inputNumbers.indices -> {
                    inputNumbers[i-1]
                }
                else -> {
                    nextToSay
                }
            }

            nextToSay = if (turnMap.containsKey(sayingNow)) {
                i - turnMap[sayingNow]!!
            } else {
                0
            }
            turnMap[sayingNow] = i
        }

        return sayingNow
    }
    override fun partTwo(): Any {
        return calculateFinalNumber(30000000)
    }
}