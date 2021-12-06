package days.aoc2020

import days.Day

class Day25: Day(2020, 25) {
    override fun partOne(): Any {
        val cardPublicKey = inputList[0].toLong()
        val doorPublicKey = inputList[1].toLong()

        val loopSize = findLoopSize(7, cardPublicKey)

        return transform(doorPublicKey, loopSize)
    }

    override fun partTwo(): Any {
        return true
    }


    fun transform(subject: Long, loopSize: Long): Long {
        var result = 1L; for (i in 0 until loopSize) result = ( result * subject ) % 20201227L
        return result
    }

    fun findLoopSize(subject: Long, target: Long): Long {
        var result = 1L
        var loops = 0L
        while (target != result) {
            result = ( result * subject ) % 20201227L
            loops++
        }

        return loops
    }

}