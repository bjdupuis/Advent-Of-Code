package days.aoc2020

import days.Day

class Day1 : Day(2020, 1) {

    override fun partOne(): Any {
        return pairs(inputList.map { it.toInt() })
                .filter { (one, two) -> one + two == 2020 }
                .map { (one, two) -> one * two }
                .first()
    }

    override fun partTwo(): Any {
        return triples(inputList.map { it.toInt() })
                .filter { (one, two, three) -> one + two + three == 2020 }
                .map { (one, two, three) -> one * two * three }
                .first()
    }

    private fun pairs(list: List<Int>): Sequence<Pair<Int,Int>> = sequence {
        for (i in list.indices)
            for (j in i + 1 until list.size - 1)
                yield(Pair(list[i], list[j]))
    }

    private fun triples(list: List<Int>): Sequence<Triple<Int,Int,Int>> = sequence {
        for (i in 0 until list.size - 2)
            for (j in i + 1 until list.size - 1)
                for (k in j + 1 until list.size)
                    yield(Triple(list[i], list[j], list[k]))
    }
}
