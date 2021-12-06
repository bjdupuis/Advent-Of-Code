package days.aoc2020

import days.Day

class Day13: Day(2020, 13) {
    override fun partOne(): Any {
        val time = inputList[0].toInt()

        return Regex("\\d+").findAll(inputList[1]).map { match -> match.groupValues[0].toInt() }
                .map { Pair(it,(it - (time % it))) }.minByOrNull { it.second }!!.let {
                    it.first * it.second
                }

    }

    override fun partTwo(): Any {
        return partTwoAnswer(inputList[1])
    }

    fun partTwoAnswer(buses: String): Long {
        val timesAndOffsets = buses.split(",").mapIndexed { index, s ->
            if (s != "x") {
                Pair(s.toLong(), index)
            } else {
                null
            }
        }.filterNotNull()

        var (increment,timestamp) = timesAndOffsets.sortedBy { it.first }.reversed().take(2)!!.let {
            val increment = it[0].first * it[1].first
            var timestamp = increment

            while(true) {
                if (((timestamp + it[0].second) % it[0].first) == 0L &&
                        ((timestamp + it[1].second) % it[1].first) == 0L) {
                    break
                } else {
                    timestamp++
                }
            }

            Pair(increment, timestamp)
        }
        var iterations = 0L
        while (true) {
            timestamp += increment

            var failed = false
            for(check in timesAndOffsets) {
                if ((timestamp + check.second) % check.first != 0L) {
                    failed = true
                    break
                }
            }

            if (iterations % 100000000L == 0L) {
                println("Current $timestamp")
            }
            if (!failed) {
                return timestamp
            }
            iterations++
        }

    }
}