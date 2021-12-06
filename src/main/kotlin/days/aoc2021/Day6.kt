package days.aoc2021

import days.Day

class Day6 : Day(2021, 6) {
    override fun partOne(): Any {
        return calculateFishAfterDays(80, inputList)
    }

    override fun partTwo(): Any {
        return calculateFishAfterDays(256, inputList)
    }

    fun calculateFishAfterDays(days: Int, list: List<String>): Long {
        val currentFishCountsByType = Array(9) { 0L }
        list.first().split(",".toRegex()).map { it.toInt() }.forEach { fish ->
            currentFishCountsByType[fish]++
        }

        for (day in 1..days) {
            val newlySpawnedFish = currentFishCountsByType[0]
            for (type in 0..7) {
                currentFishCountsByType[type] = currentFishCountsByType[type+1]
            }

            currentFishCountsByType[6] += newlySpawnedFish
            currentFishCountsByType[8] = newlySpawnedFish
        }

        return currentFishCountsByType.sum()
    }
}