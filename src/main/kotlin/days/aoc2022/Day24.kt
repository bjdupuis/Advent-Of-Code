package days.aoc2022

import days.Day
import util.Point2d

class Day24 : Day(2022, 24) {
    lateinit var blizzardBounds: Pair<IntRange, IntRange>
    override fun partOne(): Any {
        return calculateFastestRoute(inputList)
    }

    override fun partTwo(): Any {
        return calculateFastestRouteThereAndBackAndThereAgain(inputList)
    }

    fun calculateFastestRouteThereAndBackAndThereAgain(input: List<String>): Int {
        var startingPosition = Point2d(0,0)
        var endingPosition = Point2d(input[0].length - 3, input.size - 1)
        blizzardBounds = Pair(0..input[0].length - 3, 1..input.size - 2)
        val blizzards = mutableListOf<Blizzard>()
        input.drop(1).dropLast(1).forEachIndexed { y, s ->
            s.drop(1).dropLast(1).forEachIndexed { x, c ->
                val point = Point2d(x, y + 1)
                when (c) {
                    '>' -> blizzards.add(Blizzard(point, Facing.East))
                    '<' -> blizzards.add(Blizzard(point, Facing.West))
                    '^' -> blizzards.add(Blizzard(point, Facing.North))
                    'v' -> blizzards.add(Blizzard(point, Facing.South))
                }
            }
        }

        calculateFastestRouteTo(startingPosition, endingPosition, blizzards).let { (toGoalFirst, toGoalBlizzards) ->
            calculateFastestRouteTo(endingPosition, startingPosition, toGoalBlizzards).let { (toStartSteps, toStartBlizzards) ->
                calculateFastestRouteTo(startingPosition, endingPosition, toStartBlizzards).let { (toGoalSecond, _) ->
                    return toGoalFirst + toStartSteps + toGoalSecond
                }
            }
        }
    }

    private fun calculateFastestRouteTo(startingPosition: Point2d, endingPosition: Point2d, blizzards: List<Day24.Blizzard>): Pair<Int, List<Blizzard>> {
        val previousStates = mutableSetOf<PotentialMove>()
        val queue = mutableListOf<PotentialMove>()
        queue.add(PotentialMove(startingPosition, 0))
        val blizzardStates = mutableMapOf<Int,List<Blizzard>>()
        blizzardStates[0] = blizzards
        while (queue.isNotEmpty()) {
            val move = queue.removeFirst()
            if (move !in previousStates) {
                previousStates.add(move)

                val nextBlizzardState = blizzardStates[move.steps + 1] ?: run {
                    val next = blizzardStates[move.steps]!!.map { Blizzard(it.nextPosition(), it.facing) }
                    blizzardStates[move.steps + 1] = next
                    next
                }


                val potentialNextMoves =
                    move.location.neighbors().plus(move.location).filter { it !in nextBlizzardState.map { it.currentPosition } &&
                            it.isInBounds(blizzardBounds.first, blizzardBounds.second) ||
                            it == endingPosition ||
                            it == startingPosition }

                if (endingPosition in potentialNextMoves) {
                    return Pair(move.steps + 1, nextBlizzardState)
                }

                potentialNextMoves.forEach {
                    queue.add(PotentialMove(it, move.steps + 1))
                }
            }
        }

        throw IllegalStateException()
    }

    fun calculateFastestRoute(input: List<String>): Int {
        var startingPosition = Point2d(0,0)
        val endingPosition = Point2d(input[0].length - 3, input.size - 1)
        blizzardBounds = Pair(0..input[0].length - 3, 1..input.size - 2)
        val blizzards = mutableListOf<Blizzard>()
        input.drop(1).dropLast(1).forEachIndexed { y, s ->
            s.drop(1).dropLast(1).forEachIndexed { x, c ->
                val point = Point2d(x, y + 1)
                when (c) {
                    '>' -> blizzards.add(Blizzard(point, Facing.East))
                    '<' -> blizzards.add(Blizzard(point, Facing.West))
                    '^' -> blizzards.add(Blizzard(point, Facing.North))
                    'v' -> blizzards.add(Blizzard(point, Facing.South))
                }
            }
        }

        val previousStates = mutableSetOf<PotentialMove>()
        val queue = mutableListOf<PotentialMove>()
        queue.add(PotentialMove(startingPosition, 0))
        val blizzardStates = mutableMapOf<Int,List<Blizzard>>()
        blizzardStates[0] = blizzards
        while (queue.isNotEmpty()) {
            val move = queue.removeFirst()
            if (move !in previousStates) {
                previousStates.add(move)

                val nextBlizzardState = blizzardStates[move.steps + 1] ?: run {
                    val next = blizzardStates[move.steps]!!.map { Blizzard(it.nextPosition(), it.facing) }
                    blizzardStates[move.steps + 1] = next
                    next
                }


                val potentialNextMoves =
                    move.location.neighbors().plus(move.location).filter { it !in nextBlizzardState.map { it.currentPosition } &&
                            it.isInBounds(blizzardBounds.first, blizzardBounds.second) ||
                            it == endingPosition ||
                            it == startingPosition }

                if (endingPosition in potentialNextMoves) {
                    return move.steps + 1
                }

                potentialNextMoves.forEach {
                    queue.add(PotentialMove(it, move.steps + 1))
                }
            }
        }

        throw IllegalStateException()
    }

    data class PotentialMove(val location: Point2d, val steps: Int)

    inner class Blizzard(val currentPosition: Point2d, val facing: Facing) {
        fun nextPosition(): Point2d {
            var position = currentPosition + facing.delta()
            if (position.x !in blizzardBounds.first ) {
                position = position.copy(x = if (facing == Facing.East) blizzardBounds.first.first else blizzardBounds.first.last)
            } else if (position.y !in blizzardBounds.second) {
                position = position.copy(y = if (facing == Facing.South) blizzardBounds.second.first else blizzardBounds.second.last)
            }
            return position
        }
    }

    enum class Facing {
        North, South, East, West;

        fun delta(): Point2d {
            return when (this) {
                North -> Point2d(0, -1)
                South -> Point2d(0, 1)
                East -> Point2d(1, 0)
                West -> Point2d(-1, 0)
            }
        }
    }
}

fun Point2d.isInBounds(xRange: IntRange, yRange: IntRange): Boolean {
    return x in xRange && y in yRange
}