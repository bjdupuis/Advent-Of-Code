package days.aoc2023

import days.Day
import util.lcm

class Day8 : Day(2023, 8) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val directions = Directions(input[0])
        val destinations = input.drop(2).map {
            Regex("(\\w+) = \\((\\w+), (\\w+)\\)").matchEntire(it)?.destructured?.let { (source, left, right) ->
                source to Pair(left, right)
            }
        }.associate {
            it?.first to it?.second
        }

        var current = "AAA"
        var count = 0
        val iterator = directions.nextDirection.iterator()
        while (current != "ZZZ") {
            current = if (iterator.next() == 'L')
                destinations[current]!!.first
            else
                destinations[current]!!.second
            count++
        }
        return count
    }

    fun calculatePartTwo(input: List<String>): Long {
        val destinations = input.drop(2).map {
            Regex("(\\w+) = \\((\\w+), (\\w+)\\)").matchEntire(it)?.destructured?.let { (source, left, right) ->
                source to Pair(left, right)
            }
        }.associate {
            it?.first!! to it.second
        }

        return destinations.filter { it.key.endsWith('A') }.map { distanceToFirstZ(it.key!!, Directions(input[0]).nextDirection.iterator(), destinations) }.lcm()
    }

    private fun distanceToFirstZ(start: String, directions: Iterator<Char>, destinations: Map<String,Pair<String,String>>): Int {
        var current = start
        var count = 0
        do {
            current = if (directions.next() == 'L') destinations[current]!!.first else destinations[current]!!.second
            count++
        } while (!current.endsWith('Z'))

        return count
    }

    internal class Directions(private val directionList: String) {
        val nextDirection = sequence {
            do {
                directionList.forEach { yield(it) }
            } while (true)
        }
    }
}
