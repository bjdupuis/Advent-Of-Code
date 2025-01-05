package days.aoc2015

import days.Day

class Day24 : Day(2015, 24) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Long {
        val packages = input.map { it.toInt() }
        val thirdOfWeight = packages.sum() / 3

        return combinationsMatchingWeight(packages.reversed(), thirdOfWeight).groupBy { it.size }.minBy { it.key }.value.minOf { calculateQuantumEntanglement(it) }
    }

    private fun calculateQuantumEntanglement(packages: Set<Int>): Long {
        return packages.fold(1L) { acc, i -> i * acc }
    }

    private fun combinationsMatchingWeight(packages: List<Int>, weight: Int) = sequence {
        val queue = mutableListOf<Pair<Set<Int>,Set<Int>>>()
        packages.forEachIndexed { index, p ->
            if (index != packages.lastIndex) {
                val remaining = packages.subList(index + 1, packages.lastIndex + 1)
                if (remaining.sum() + p >= weight) {
                    queue.add(
                        setOf(p) to packages.subList(index + 1, packages.lastIndex + 1).toSet()
                    )
                }
            }
        }

        var minimumPackagesSeen = Int.MAX_VALUE
        while(queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current.first.sum() == weight) {
                println("Yielding ${current.first}")
                if (current.first.size <= minimumPackagesSeen) {
                    minimumPackagesSeen = current.first.size
                    yield(current.first)
                }
            } else if (current.first.sum() < weight && current.first.size + 1 <= minimumPackagesSeen) {
                current.second.reversed().filter { it + current.first.sum() <= weight }.forEach {
                    queue.addFirst(current.first + it to current.second.minus(it))
                }
            }
        }
    }

    fun calculatePartTwo(input: List<String>): Long {
        val packages = input.map { it.toInt() }
        val quarterOfWeight = packages.sum() / 4

        return combinationsMatchingWeight(packages.reversed(), quarterOfWeight).groupBy { it.size }.minBy { it.key }.value.minOf { calculateQuantumEntanglement(it) }
    }
}