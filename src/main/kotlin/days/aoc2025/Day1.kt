package days.aoc2025

import days.Day

class Day1 : Day(2025, 1) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        var dial = 50
        var dialIsZero = 0

        for (line in input) {
            val direction = line[0]
            val amount = line.drop(1).toInt() % 100
            if (direction == 'L') {
                dial -= amount
                if (dial < 0) {
                    dial += 100
                }
            } else {
                dial += amount
                if (dial > 99) {
                    dial -= 100
                }
            }

            if (dial == 0) {
                dialIsZero++
            }
        }

        return dialIsZero
    }

    fun calculatePartTwo(input: List<String>): Int {
        var dial = 50
        var dialIsZero = 0

        for (line in input) {
            val direction = line[0]
            val amount = line.drop(1).toInt()
            var amountTurned = 0
            while (amountTurned < amount) {
                val amountToTurn = if (amount - amountTurned > 100) 100 else amount - amountTurned
                val dontCount = dial == 0
                if (direction == 'L') {
                    dial -= amountToTurn

                    if (dial < 0) {
                        if (!dontCount) {
                            dialIsZero++
                        }
                        dial += 100
                    }

                } else {
                    dial += amountToTurn
                    if (dial > 99) {
                        dial -= 100
                        if (!dontCount && dial != 0) {
                            dialIsZero++
                        }
                    }
                }
                if (dial == 0) {
                    dialIsZero++
                }
                amountTurned += amountToTurn
            }
        }

        return dialIsZero
    }
}