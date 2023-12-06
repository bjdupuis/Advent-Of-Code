package days.aoc2023

import days.Day

class Day6 : Day(2023, 6) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    fun calculatePartOne(inputList: List<String>): Int {
        val timesAndDistances =
            inputList[0].split("\\s+".toRegex()).drop(1).map { it.toInt() }.zip(
                    inputList[1].split("\\s+".toRegex()).drop(1).map { it.toInt() })

        return timesAndDistances.fold(1) { current, race ->
            current * calculateWaysToWin(race.first.toLong(), race.second.toLong())
        }
    }

    private fun calculateWaysToWin(time: Long, distance: Long): Int {
        return (0..time).count { millisHeldDown ->
            millisHeldDown * (time - millisHeldDown) > distance
        }
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartTwo(inputList: List<String>): Int {
        val time = inputList[0].dropWhile { it != ':' }.drop(1).replace("\\s+".toRegex(), "").toLong()
        val distance = inputList[1].dropWhile { it != ':' }.drop(1).replace("\\s+".toRegex(), "").toLong()

        return calculateWaysToWin(time, distance)
    }
}
