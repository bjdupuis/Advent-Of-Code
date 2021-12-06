package days.aoc2020

import days.Day

class Day9: Day(2020, 9) {
    override fun partOne(): Any {
        return findFirstFailure(25)
    }

    override fun partTwo(): Any {
        return findContiguousSetFor(findFirstFailure(25))
    }

    fun findContiguousSetFor(firstFailure: Int): Int {
        inputList.map {
            it.toInt()
        }.let { intList ->
            for(windowSize in 2 until intList.size / 2) {
                intList.windowed(windowSize).let { windows ->
                    windows.forEach { window ->
                        if (window.sum() == firstFailure) {
                            return (window.minOrNull()!! + window.maxOrNull()!!)
                        }
                    }
                }
            }
        }

        return -1
    }

    fun findFirstFailure(preambleSize: Int): Int {
        for (i in preambleSize until inputList.size) {
            val current = inputList[i].toInt()
            inputList.subList(i - preambleSize, i).map {
                it.toInt()
            }.toSet().let { windowSet ->
                windowSet.firstOrNull { windowSet.minus(it).contains(current - it) }
                        ?: return current
            }
        }

        return -1
    }
}

