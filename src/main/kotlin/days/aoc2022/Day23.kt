package days.aoc2022

import days.Day
import util.Point2d

class Day23 : Day(2022, 23) {
    override fun partOne(): Any {
        return calculateEmptyLandOnMinimalRectangleAfterRounds(inputList, 10)
    }

    override fun partTwo(): Any {
        return calculateRoundWithNoMovingElves(inputList)
    }

    fun calculateRoundWithNoMovingElves(input: List<String>): Int {
        val map = mutableMapOf<Point2d, Char>()
        for (y in input.indices) {
            val line = input[y]
            for (x in line.indices) {
                map[Point2d(x, y)] = line[x]
            }
        }

        val proposals = mutableMapOf<Point2d, Point2d>()
        var start = 0
        var elvesMoving = true
        while (elvesMoving) {
            map.filter { it.value == '#' }.keys.forEach { elf ->
                if (neighbors(elf).any { '#' == map[it] }) {
                    var proposal: Point2d? = null
                    var i = 1

                    // reset considerations
                    val considerations = considerations(elf, start)
                    while (i++ <= 4 && proposal == null) {
                        val direction = considerations.next()
                        if (direction.none { '#' == map[it] }) {
                            proposal = direction.first()
                        }
                    }

                    if (proposal != null) {
                        proposals[elf] = proposal
                    }
                }
            }

            val validProposals = proposals.filter { proposal -> proposals.count { it.value == proposal.value } == 1 }
            if (validProposals.isEmpty()) {
                elvesMoving = false
            } else {
                validProposals.forEach { proposal ->
                    map[proposal.value] = '#'
                    map[proposal.key] = '.'
                }
                proposals.clear()
            }
            start++
        }

        return start
    }

    fun calculateEmptyLandOnMinimalRectangleAfterRounds(input: List<String>, rounds: Int): Int {
        val map = mutableMapOf<Point2d, Char>()
        for (y in input.indices) {
            val line = input[y]
            for (x in line.indices) {
                map[Point2d(x, y)] = line[x]
            }
        }

        val proposals = mutableMapOf<Point2d, Point2d>()
        var start = 0
        repeat(rounds) {
            map.filter { it.value == '#' }.keys.forEach { elf ->
                if (neighbors(elf).any { '#' == map[it] }) {
                    var proposal: Point2d? = null
                    var i = 1

                    // reset considerations
                    val considerations = considerations(elf, start)
                    while (i++ <= 4 && proposal == null) {
                        val direction = considerations.next()
                        if (direction.none { '#' == map[it] }) {
                            proposal = direction.first()
                        }
                    }

                    if (proposal != null) {
                        proposals[elf] = proposal
                    }
                }
            }

            proposals.filter { proposal -> proposals.count { it.value == proposal.value } == 1 }.forEach { proposal ->
                map[proposal.value] = '#'
                map[proposal.key] = '.'
            }
            proposals.clear()
            start++

            val elves = map.filter { it.value == '#' }
            val min = Point2d(elves.keys.minOf { it.x }, elves.keys.minOf { it.y })
            val max = Point2d(elves.keys.maxOf { it.x }, elves.keys.maxOf { it.y })

            for (y in min.y..max.y) {
                for (x in min.x..max.x) {
                    print(map[Point2d(x, y)] ?: ".")
                }
                println()
            }
            println()

        }

        val elves = map.filter { it.value == '#' }
        val min = Point2d(elves.keys.minOf { it.x }, elves.keys.minOf { it.y })
        val max = Point2d(elves.keys.maxOf { it.x }, elves.keys.maxOf { it.y })

        var count = 0
        for (x in min.x..max.x) {
            for (y in min.y..max.y) {
                if ('.' == (map[Point2d(x, y)] ?: '.')) {
                    count++
                }
            }
        }
        return count
    }

    private fun considerNorth(fromLocation: Point2d): List<Point2d> {
        return listOf(
            fromLocation.copy(y = fromLocation.y - 1),
            fromLocation.copy(x = fromLocation.x - 1, y = fromLocation.y - 1),
            fromLocation.copy(x = fromLocation.x + 1, y = fromLocation.y - 1))
    }
    private fun considerSouth(fromLocation: Point2d): List<Point2d> {
        return listOf(
            fromLocation.copy(y = fromLocation.y + 1),
            fromLocation.copy(x = fromLocation.x - 1, y = fromLocation.y + 1),
            fromLocation.copy(x = fromLocation.x + 1, y = fromLocation.y + 1))
    }
    private fun considerWest(fromLocation: Point2d): List<Point2d> {
        return listOf(
            fromLocation.copy(x = fromLocation.x - 1),
            fromLocation.copy(x = fromLocation.x - 1, y = fromLocation.y - 1),
            fromLocation.copy(x = fromLocation.x - 1, y = fromLocation.y + 1))
    }
    private fun considerEast(fromLocation: Point2d): List<Point2d> {
        return listOf(
            fromLocation.copy(x = fromLocation.x + 1),
            fromLocation.copy(x = fromLocation.x + 1, y = fromLocation.y - 1),
            fromLocation.copy(x = fromLocation.x + 1, y = fromLocation.y + 1))
    }

    private fun neighbors(fromLocation: Point2d): List<Point2d> {
        val result = mutableSetOf<Point2d>()
        result.addAll(considerEast(fromLocation))
        result.addAll(considerNorth(fromLocation))
        result.addAll(considerSouth(fromLocation))
        result.addAll(considerWest(fromLocation))
        return result.toList()
    }


    private fun considerations(fromLocation: Point2d, start: Int) = sequence {
        val list = listOf(
            considerNorth(fromLocation),
            considerSouth(fromLocation),
            considerWest(fromLocation),
            considerEast(fromLocation))

        var i = 0
        while(true) {
            yield(list[(start + i++) % 4])
        }
    }.iterator()

}