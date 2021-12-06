package days.aoc2020

import days.Day

class Day5: Day(2020, 5) {
    override fun partOne(): Any {
        return inputList.map { boardingPass ->
            calculateSeatId(boardingPass)
        }.maxOrNull()!!
    }

    fun calculateSeatId(boardingPass:String): Int {
        val seatId = calculateRow(boardingPass) * 8 + calculateSeat(boardingPass)
        return seatId
    }

    fun calculateRow(boardingPass: String): Int {
        val row = boardingPass.substring(0..6).mapIndexed { index, c ->
            if (c == 'B') {
                1 shl (6 - index)
            } else {
                0
            }
        }.sum()
        return row
    }

    fun calculateSeat(boardingPass: String): Int {
        val seat = boardingPass.substring(7..9).mapIndexed { index, c ->
            if (c == 'R') {
                1 shl (2 - index)
            } else {
                0
            }
        }.sum()
        return seat
    }

    override fun partTwo(): Any {
        return inputList.map { calculateSeatId(it) }
                .toSet()
                .let { seatIdSet ->
                    sequence {
                        for (i in seatIdSet.minOrNull()!!.until(seatIdSet.maxOrNull()!!)) {
                            yield(i)
                        }
                    }.toSet()
                            .subtract(seatIdSet)
                            .first()
                }
    }
}