package days.aoc2025

import days.Day
import util.Point3d

class Day8 : Day(2025, 8) {
    override fun partOne(): Any {
        return calculatePartOne(1000, inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(numberOfConnections: Int, input: List<String>): Int {
        val points = input.map { Regex("(\\d+),(\\d+),(\\d+)").matchEntire(it)?.destructured?.let { (x,y,z) ->
            Point3d(x.toInt(), y.toInt(), z.toInt())
        } }

        var distanceMap = mutableMapOf<Pair<Point3d, Point3d>,Double>()
        points.forEachIndexed { index, p1 ->
            for (i in index + 1 .. points.lastIndex) {
                val p2 = points[i]!!
                distanceMap[Pair(p1!!, p2)] = p1.euclidianDistanceTo(p2)
            }
        }

        val sorted = distanceMap.toList()
            .sortedBy { (_, value) -> value }
            .toMap()

        val circuits = mutableListOf<MutableSet<Point3d>>()
        sorted.entries.take(numberOfConnections).forEach { (pair, _) ->
            var added = false

            for (circuit in circuits) {
                if (circuit.contains(pair.first) || circuit.contains(pair.second)) {
                    circuit.add(pair.first)
                    circuit.add(pair.second)
                    added = true
                    break
                }
            }

            if (!added) {
                circuits.add(mutableSetOf(pair.first, pair.second))
            }

            if (circuits.size > 1) {
                var done = false
                while (!done) {
                    var merged = false
                    outer@for (i in 0..circuits.lastIndex - 1) {
                        for (j in i + 1..circuits.lastIndex) {
                            if (circuits[i].any { circuits[j].contains(it) }) {
                                circuits[i].addAll(circuits[j])
                                circuits.remove(circuits[j])
                                merged = true
                                break@outer
                            }
                        }
                    }
                    if (!merged) {
                        done = true
                    }
                }
            }
        }

        return circuits.map { it.size }.sorted().reversed().take(3).reduce { acc, i -> acc * i }
    }

    fun calculatePartTwo(input: List<String>): Long {
        val points = input.map { Regex("(\\d+),(\\d+),(\\d+)").matchEntire(it)?.destructured?.let { (x,y,z) ->
            Point3d(x.toInt(), y.toInt(), z.toInt())
        } }

        var distanceMap = mutableMapOf<Pair<Point3d, Point3d>,Double>()
        points.forEachIndexed { index, p1 ->
            for (i in index + 1 .. points.lastIndex) {
                val p2 = points[i]!!
                distanceMap[Pair(p1!!, p2)] = p1.euclidianDistanceTo(p2)
            }
        }

        val sorted = distanceMap.toList()
            .sortedBy { (_, value) -> value }
            .toMap()

        val circuits = mutableListOf<MutableSet<Point3d>>()
        sorted.entries.forEach { (pair, _) ->
            var added = false

            for (circuit in circuits) {
                if (circuit.contains(pair.first) || circuit.contains(pair.second)) {
                    circuit.add(pair.first)
                    circuit.add(pair.second)
                    added = true
                    break
                }
            }

            if (!added) {
                circuits.add(mutableSetOf(pair.first, pair.second))
            }

            if (circuits.size > 1) {
                var done = false
                while (!done) {
                    var merged = false
                    outer@for (i in 0..circuits.lastIndex - 1) {
                        for (j in i + 1..circuits.lastIndex) {
                            if (circuits[i].any { circuits[j].contains(it) }) {
                                circuits[i].addAll(circuits[j])
                                circuits.remove(circuits[j])
                                merged = true

                                break@outer
                            }
                        }
                    }
                    if (!merged) {
                        done = true
                    }
                }
            }

            if (circuits.size == 1 && circuits.first().size == points.size) {
                return pair.first.x.toLong() * pair.second.x.toLong()
            }

        }

        throw IllegalStateException("Shouldn't have finished this way")
    }
}