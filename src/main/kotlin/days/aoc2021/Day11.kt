package days.aoc2021

import days.Day

class Day11 : Day(2021, 11) {
    override fun partOne(): Any {
        return calculateFlashesAfterSteps(inputList, 100)
    }

    override fun partTwo(): Any {
        return calculateFirstSimultaneousFlashStep(inputList)
    }

    fun calculateFirstSimultaneousFlashStep(inputLines: List<String>): Int {
        val octopi = Array(inputLines.size) {
            Array(inputLines.first().length) { 0 }
        }
        for (y in octopi.indices) {
            for (x in octopi.first().indices) {
                octopi[y][x] = inputLines[y][x].toString().toInt()
            }
        }

        var flashes: Int
        var steps = 0
        do {
            flashes = 0
            steps++
            for (y in octopi.indices) {
                for (x in octopi.first().indices) {
                    octopi[y][x]++
                    if (octopi[y][x] == 10) {
                        flash(y, x, octopi)
                    }
                }
            }

            for (y in octopi.indices) {
                for (x in octopi.first().indices) {
                    if (octopi[y][x] > 9) {
                        flashes++
                        octopi[y][x] = 0
                    }
                }
            }
        } while (flashes != octopi.size * octopi.first().size)

        return steps
    }

    fun calculateFlashesAfterSteps(inputLines: List<String>, steps: Int): Int {
        val octopi = Array(inputLines.size) {
            Array(inputLines.first().length) { 0 }
        }
        for (y in octopi.indices) {
            for (x in octopi.first().indices) {
                octopi[y][x] = inputLines[y][x].toString().toInt()
            }
        }

        var flashes = 0
        for (step in 1..steps) {
            for (y in octopi.indices) {
                for (x in octopi.first().indices) {
                    octopi[y][x]++
                    if (octopi[y][x] == 10) {
                        flash(y, x, octopi)
                    }
                }
            }

            for (y in octopi.indices) {
                for (x in octopi.first().indices) {
                    if (octopi[y][x] > 9) {
                        flashes++
                        octopi[y][x] = 0
                    }
                }
            }
        }

        return flashes
    }

    private fun flash(atY: Int, atX: Int, octopi: Array<Array<Int>>) {
        for (y in (atY - 1)..(atY + 1)) {
            if (y in octopi.indices) {
                for (x in (atX - 1)..(atX + 1)) {
                    if (x in octopi[y].indices && (x != atX || y != atY)) {
                        if (++octopi[y][x] == 10) {
                            flash(y, x, octopi)
                        }
                    }
                }
            }
        }
    }


}