package days.aoc2021

import days.Day
import java.lang.Integer.max
import kotlin.math.absoluteValue
import kotlin.math.min

class Day5 : Day(2021, 5) {
    override fun partOne(): Any {
        return calculateMostDangerousPoints(createMapWithNonDiagonals(inputList))
    }

    override fun partTwo(): Any {
        return calculateMostDangerousPoints(createMapIncludingDiagonals(inputList))
    }

    private fun findExtentsOfMap(input: List<String>): Pair<Int,Int> {
        return input.fold(Pair(0,0)) { acc, s ->
            Regex("(\\d+),(\\d+) -> (\\d+),(\\d+)").matchEntire(s)?.destructured?.let { (x1,y1,x2,y2) ->
                Pair(maxOf(acc.first, x1.toInt(), x2.toInt()), maxOf(acc.second, y1.toInt(), y2.toInt()))
            } ?: acc
        }
    }

    fun createMapWithNonDiagonals(input: List<String>): Array<Array<Int>> {
        val extents = findExtentsOfMap(input)
        val map = Array(extents.first + 1) {
            Array(extents.second + 1) { 0 }
        }

        input.mapNotNull { line ->
            Regex("(\\d+),(\\d+) -> (\\d+),(\\d+)").matchEntire(line)?.destructured?.let { (x1, y1, x2, y2) ->
                Pair(Pair(x1.toInt(), y1.toInt()), Pair(x2.toInt(), y2.toInt()))
            }
        }.filter {
            it.first.first == it.second.first || it.first.second == it.second.second
        }.forEach {
            for (x in min(it.first.first,it.second.first).rangeTo(max(it.first.first,it.second.first))) {
                for (y in min(it.first.second,it.second.second).rangeTo(max(it.first.second,it.second.second))) {
                    map[x][y]++
                }
            }
        }

        return map
    }

    fun createMapIncludingDiagonals(input: List<String>): Array<Array<Int>> {
        val extents = findExtentsOfMap(input)
        val map = Array(extents.first + 1) {
            Array(extents.second + 1) { 0 }
        }

        input.mapNotNull { line ->
            println("Processing $line")
            Regex("(\\d+),(\\d+) -> (\\d+),(\\d+)").matchEntire(line.trim())?.destructured?.let { (x1, y1, x2, y2) ->
                Pair(Pair(x1.toInt(), y1.toInt()), Pair(x2.toInt(), y2.toInt()))
            }
        }.forEach {
            println("Time for $it")
            if (it.first.first == it.second.first || it.first.second == it.second.second) {
                for (x in min(it.first.first,it.second.first).rangeTo(max(it.first.first,it.second.first))) {
                    for (y in min(it.first.second,it.second.second).rangeTo(max(it.first.second,it.second.second))) {
                        map[x][y]++
                    }
                }
            } else {
                val steps = (it.first.first - it.second.first).absoluteValue
                val deltaX = if (it.first.first < it.second.first) 1 else -1
                val deltaY = if (it.first.second < it.second.second) 1 else -1
                var x = it.first.first
                var y = it.first.second
                for (i in 0..steps) {
                    map[x][y]++
                    x += deltaX
                    y += deltaY
                }
            }
        }

        return map
    }

    fun printMap(map:Array<Array<Int>>) {
        for (y in map.indices) {
            for (x in map[y].indices)
                print(if (map[x][y] == 0) '.' else map[x][y])
            println()
        }
    }

    fun calculateMostDangerousPoints(map: Array<Array<Int>>): Int {
        return map.sumBy { it.sumBy { dangerLevel -> if (dangerLevel >= 2) 1 else 0  } }
    }
}