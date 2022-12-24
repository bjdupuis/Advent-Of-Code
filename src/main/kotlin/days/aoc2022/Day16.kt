package days.aoc2022

import days.Day
import kotlin.math.max

class Day16 : Day(2022, 16) {
    override fun partOne(): Any {
        return calculateMaxPressureReliefOverTime(inputList, 30)
    }

    override fun partTwo(): Any {
        return calculateMaxPressureReliefOverTimeWithTwoParticipants(inputList, 26)
    }

    fun calculateMaxPressureReliefOverTimeWithTwoParticipants(input: List<String>, minutes: Int): Int {
        val valves = parseValves(input)
        val paths = calculateShortestPathsToEachNonzeroValve(valves)
        return calculateHighestScore(
            valves.first { it.name == "AA"},
            paths,
            setOf(),
            minutes,
            0,
            true
        )
    }

    fun calculateMaxPressureReliefOverTime(input: List<String>, minutes: Int): Int {
        val valves = parseValves(input)
        return calculateHighestScore(
            valves.first { it.name == "AA"},
            calculateShortestPathsToEachNonzeroValve(valves),
            setOf(),
            minutes,
            0
        )
    }

    private fun calculateHighestScore(current: Valve, shortestPaths: Map<Valve, Map<Valve, Int>>, visited: Set<Valve>, timeLeft: Int, score: Int, isElephantInvolved: Boolean = false): Int {
        var max = score
        for (next in shortestPaths[current]!!) {
            if (next.key !in visited && timeLeft > 1 + next.value) {
                max = max(max, calculateHighestScore(
                    next.key,
                    shortestPaths,
                    visited + next.key,
                    (timeLeft - next.value) - 1,
                    score + (next.key.flowRate * ((timeLeft - next.value) - 1)),
                    isElephantInvolved
                ))
            }
        }
        return if (isElephantInvolved) {
            max(max, calculateHighestScore(shortestPaths.keys.first { it.name == "AA" }, shortestPaths, visited, 26, score))
        } else {
            max
        }
    }

    private fun calculateShortestPathsToEachNonzeroValve(valves: List<Valve>): Map<Valve, Map<Valve, Int>> {
        val result = valves.associateWith { valve ->
            valve.leadsTo.map { valveName -> valves.first { it.name == valveName } }
                .associateWith { 1 }.toMutableMap()
        }.toMutableMap()

        for (k in valves) {
            for (j in valves) {
                for (i in valves) {
                    val ij = result[i]?.get(j) ?: 5000
                    val ik = result[i]?.get(k) ?: 5000
                    val kj = result[k]?.get(j) ?: 5000
                    if (ik + kj < ij) {
                        result[i]?.set(j, ik + kj)
                    }
                }
            }
        }

        result.forEach { (valve, paths) ->
            paths.keys.filter { it.flowRate == 0 }.forEach {
                result[valve]!!.remove(it)
            }
        }

        return result
    }

    // Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
    private fun parseValves(input: List<String>): List<Valve> {
        return input.map { line ->
                Regex("Valve (\\w+) has flow rate=(\\d+); tunnel[s]? lead[s]? to valve[s]? (.*)").matchEntire(line)
                    ?.destructured?.let { (name, flowRate, destinations) ->
                        val leadsTo = mutableListOf<String>()
                        destinations.split(", ").forEach { destination ->
                            leadsTo.add(destination)
                        }
                        Valve(name, flowRate.toInt(), leadsTo)
                    } ?: throw IllegalStateException()
        }
    }

    class Valve(val name: String, val flowRate: Int, val leadsTo: List<String>)

}