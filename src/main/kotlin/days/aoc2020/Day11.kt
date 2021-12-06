package days.aoc2020

import days.Day

class Day11: Day(2020, 11) {
    override fun partOne(): Any {
        var lastValue = 0
        var currentValue = 1
        var seatLayout = mutableListOf<String>()
        seatLayout.addAll(inputList)

        while(currentValue != lastValue) {
            lastValue = currentValue
            seatLayout = calculateNextLayout(seatLayout, 4, ::adjacentSequence)
            currentValue = seatLayout.map { it.count { it == '#' } }.sum()
        }

        return currentValue
    }

    fun calculateNextLayout(seatLayout: List<String>, limit: Int, sequenceFunction: (List<String>, Pair<Int, Int>) -> Sequence<Char>): MutableList<String> {
        val newLayout = mutableListOf<String>()
        for (y in seatLayout.indices) {
            val row = seatLayout[y]
            var newRow = ""
            for (x in row.indices) {
                val seat = row[x]
                newRow += if (seat != '.') {
                    val adjacentPeople = sequenceFunction(seatLayout, Pair(x,y)).count { it == '#' }
                    when {
                        adjacentPeople >= limit -> {
                            'L'
                        }
                        adjacentPeople == 0 -> {
                            '#'
                        }
                        else -> {
                            seat
                        }
                    }
                } else {
                    seat
                }
            }
            newLayout.add(newRow)
        }

        return newLayout
    }

    override fun partTwo(): Any {
        var lastValue = 0
        var currentValue = 1
        var seatLayout = mutableListOf<String>()
        seatLayout.addAll(inputList)

        while(currentValue != lastValue) {
            lastValue = currentValue
            seatLayout = calculateNextLayout(seatLayout, 5, ::visibleSequence)
            currentValue = seatLayout.map { it.count { it == '#' } }.sum()
        }

        return currentValue
    }
}

fun adjacentSequence(seatLayout: List<String>, position: Pair<Int,Int>) = sequence {
    for (y in (position.second - 1)..(position.second+1)) {
        if (y !in seatLayout.indices) continue
        val row = seatLayout[y]
        for (x in (position.first - 1)..(position.first+1)) {
            if (x !in row.indices || (x == position.first && y == position.second)) continue
            yield(row[x])
        }
    }
}

fun visibleSequence(seatLayout: List<String>, position: Pair<Int,Int>) = sequence {
    val steps = listOf(
            Pair(-1, -1),   Pair(0, -1),    Pair(1, -1),
            Pair(-1, 0),                    Pair(1, 0),
            Pair(-1, 1),    Pair(0, 1),     Pair(1, 1))

    for (step in steps) {
        var done = false
        var newValue = position
        while (!done) {
            newValue = Pair(newValue.first + step.first, newValue.second + step.second)
            if (newValue.first !in seatLayout[0].indices || newValue.second !in seatLayout.indices) {
                done = true
                continue
            }

            val seat = seatLayout[newValue.second][newValue.first]
            if (seat != '.') {
                done = true
                yield(seat)
            }
        }
    }
}
