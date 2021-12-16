package days.aoc2021

import days.Day
import java.util.*
import kotlin.Comparator

class Day15 : Day(2021, 15) {
    override fun partOne(): Any {
        val cave = createCave(inputList)
        return calculateLowestRiskPath(cave)
    }

    override fun partTwo(): Any {
        val cave = createFullCave(createCave(inputList))
        return calculateLowestRiskPath(cave)
    }

    fun createCave(inputList: List<String>) : Array<Array<Int>> {
        val cave = Array(inputList.first().length) {
            Array(inputList.size) { 0 }
        }

        inputList.forEachIndexed { y, line ->
            line.trim().forEachIndexed { x, c ->
                cave[x][y] = c - '0'
            }
        }

        return cave
    }

    fun createFullCave(subset: Array<Array<Int>>): Array<Array<Int>> {
        val xSize = subset.first().size
        val ySize = subset.size
        val cave = Array(xSize * 5) {
            Array(ySize * 5) { 0 }
        }
        for (y in 0 until 5) {
            for (x in 0 until 5) {
                val increment = x + y
                subset.forEachIndexed { originalY, row ->
                    row.forEachIndexed { originalX, originalValue ->
                        var newValue = originalValue + increment
                        if (newValue > 9) {
                            newValue -= 9
                        }
                        cave[x * xSize + originalX][y * ySize + originalY] = newValue
                    }
                }
            }
        }

        return cave
    }

    fun calculateLowestRiskPath(cave: Array<Array<Int>>): Int {

        val goal = Pair(cave.first().lastIndex, cave.lastIndex)
        val comparator: Comparator<CaveNode> = compareBy { it.priority }
        val frontier = PriorityQueue(comparator)
        frontier.add(CaveNode(Pair(0,0), 0))
        val risks = mutableMapOf<Pair<Int,Int>, Int>()
        risks[Pair(0,0)] = 0

        while (frontier.isNotEmpty()) {
            val current = frontier.remove()
            //println("Looking at ${current.location} (risk at that location is ${cave[current.location.first][current.location.second]})")

            if (current.location == goal) {
                return risks[current.location]!!
            }

            getNeighbors(current.location.first, current.location.second, cave).forEach { neighbor ->
                val risk = risks[current.location]!! + cave[neighbor.first][neighbor.second]
                //print("... risk moving to $neighbor is $risk")

                if (!risks.containsKey(neighbor) || risk < risks[neighbor]!!) {
                    //print(" and it's the low")
                    risks[neighbor] = risk
                    frontier.add(CaveNode(neighbor, risk))
                }

                //println()
            }
        }

        return 0
    }

    class CaveNode(val location: Pair<Int,Int>, var priority: Int)

    fun getNeighbors(x: Int, y: Int, cave: Array<Array<Int>>) = sequence {
        if (x != 0)
            yield(Pair(x - 1, y))
        if (x != cave.first().lastIndex)
            yield(Pair(x + 1, y))
        if (y != 0)
            yield(Pair(x, y - 1))
        if (y != cave.lastIndex)
            yield(Pair(x, y + 1))
    }
}