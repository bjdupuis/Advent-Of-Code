package days.aoc2015

import days.Day

class Day14: Day(2015, 14) {
    // Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds

    override fun partOne(): Any {
        val reindeer = parseReindeer(inputList)

        return reindeer.map { it.calculateDistanceForTime(2503) }.maxOrNull() ?: 0
    }

    fun parseReindeer(list: List<String>): List<Reindeer> {
        return list.map {
            Regex("(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.").matchEntire(it)?.destructured?.let { (name, speed, flightTime, restTime) ->
                Reindeer(name, speed.toInt(), flightTime.toInt(), restTime.toInt())
            }!!
        }
    }

    override fun partTwo(): Any {
        val reindeer = parseReindeer(inputList)

        return calculateWinningScore(reindeer, 2503)
    }

    fun calculateWinningScore(reindeer: List<Reindeer>, time: Int): Int {
        for (i in 1..time) {
            val max = reindeer.map { reindeer -> reindeer.calculateDistanceForTime(i) }.maxOrNull()
            reindeer.filter { reindeer -> reindeer.calculateDistanceForTime(i) == max }.forEach { reindeer -> reindeer.score++ }
        }

        return reindeer.maxByOrNull { reindeer -> reindeer.score }!!.score
    }


    class Reindeer(val name: String, private val speed: Int, private val flightTime: Int, private val restTime: Int) {
        var score = 0

        fun calculateDistanceForTime(seconds: Int): Int {
            return ((seconds / (flightTime + restTime)) * (speed * flightTime)) + (minOf(flightTime, (seconds % ( flightTime + restTime ))) * speed)
        }
    }
}