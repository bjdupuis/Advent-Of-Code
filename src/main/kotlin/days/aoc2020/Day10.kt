package days.aoc2020

import days.Day

class Day10: Day(2020, 10) {
    override fun partOne(): Any {
        inputList.map {
            it.toInt()
        }.plus(0).sorted().windowed(2).map {
            it[1] - it[0]
        }.let {
            return (it.count { it == 3 } + 1) * it.count {it == 1}
        }
    }

    override fun partTwo(): Any {
        val list = inputList.map { it.toInt() }.plus(0).sorted()

        return findAdaptersTo(list.plus(list.max()!! + 3), list.max()!! + 3)
    }

    private val cache = mutableMapOf<Int, Long>()
    fun findAdaptersTo(list: List<Int>, adapter: Int): Long {
        if (adapter == 0) {
            return 1
        }
        if (cache.containsKey(adapter)) {
            return cache[adapter]!!
        } else {
            var total = 0L
            for (i in (adapter - 3)..(adapter - 1)) {
                if (list.contains(i)) {
                    total += findAdaptersTo(list, i)
                }
            }
            cache[adapter] = total
        }

        return cache[adapter]!!
    }

}