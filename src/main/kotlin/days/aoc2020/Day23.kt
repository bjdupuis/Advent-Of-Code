package days.aoc2020

import days.Day

class Day23: Day(2020, 23) {
    override fun partOne(): Any {
        val solution = solve(inputString, 100, 9)

        return solution.joinToString("").toInt()
    }

    override fun partTwo(): Any {
        return solve(inputString, 10000000, 1000000).subList(0,2).let {
            it[0].toLong() * it[1]
        }
    }

    // 389125467
    // [2,5,8,6,4,7,10,9,1,11]
    // array is a map from any cup label to its clockwise neighbor
    fun solve(startingString: String, steps: Int, overallSize: Int): List<Int> {
        val nextCups = Array(overallSize+1) { it + 1 }
        val starter = startingString.map { Character.digit(it, 10) }
        starter.forEachIndexed { index, i -> nextCups[i] = starter.getOrElse(index+1) { if (overallSize > 9) 10 else starter[0] } }
        if (overallSize > 10) {
            nextCups[overallSize] = starter[0]
        }

        var currentCup = starter[0]
        for (i in 1..steps) {
            val drawnCups = listOf(nextCups[currentCup], nextCups[nextCups[currentCup]], nextCups[nextCups[nextCups[currentCup]]])

            var destination = currentCup - 1
            while (drawnCups.contains(destination) || destination < 1) {
                if (destination > 1) {
                    destination--
                } else {
                    destination = overallSize
                }
            }

            nextCups[currentCup] = nextCups[drawnCups[2]]
            nextCups[drawnCups[2]] = nextCups[destination]
            nextCups[destination] = drawnCups[0]

            currentCup = nextCups[currentCup]
        }

        currentCup = 1
        return (1..8).map {
            val result = nextCups[currentCup]
            currentCup =  result
            result
        }
    }
}
