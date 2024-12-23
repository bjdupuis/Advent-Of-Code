package days.aoc2024

import days.Day
import util.Cliques

class Day23 : Day(2024, 23) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val computerConnectionsMap = mutableMapOf<String,Set<String>>()
        input.forEach { line ->
            line.split("-").let { (first, second) ->
                var set = computerConnectionsMap[first] ?: setOf()
                computerConnectionsMap[first] = set + second
                set = computerConnectionsMap[second] ?: setOf()
                computerConnectionsMap[second] = set + first
            }
        }

        val networks = mutableSetOf<Set<String>>()
        computerConnectionsMap.filterKeys { it.startsWith("t") }.map { first ->
            first.value.forEach { second ->
                computerConnectionsMap[second]?.intersect(first.value)?.let { intersection ->
                    intersection.forEach { third ->
                        networks.add(setOf(first.key, second, third))
                    }
                }
            }
        }
        return networks.count()
    }

    fun calculatePartTwo(input: List<String>): String {
        val computerConnectionsMap = mutableMapOf<String,Set<String>>()
        input.forEach { line ->
            line.split("-").let { (first, second) ->
                val firstSet = computerConnectionsMap[first] ?: setOf()
                computerConnectionsMap[first] = firstSet + second
                val secondSet = computerConnectionsMap[second] ?: setOf()
                computerConnectionsMap[second] = secondSet + first
            }
        }

        return Cliques<String>().findMaximalClique(computerConnectionsMap, computerConnectionsMap.keys.toMutableSet()).sorted().joinToString(",")
    }
}