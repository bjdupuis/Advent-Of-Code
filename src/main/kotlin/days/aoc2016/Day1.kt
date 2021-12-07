package days.aoc2016

import days.Day
import kotlin.math.absoluteValue

class Day1 : Day(2016, 1) {
    override fun partOne(): Any {
        return calculateDistanceForDirections(inputString)
    }

    override fun partTwo(): Any {
        return findFirstLocationVisitedTwice(inputString)
    }

    fun calculateDistanceForDirections(directions: String): Int {
        var heading = Heading.NORTH
        var position = Pair(0,0)

        directions.split(", ".toRegex()).forEach { step ->
            Regex("([RL])(\\d+)").matchEntire(step.trim())?.destructured?.let { (direction, distance) ->
                heading = when (heading) {
                    Heading.NORTH -> if (direction == "R") Heading.EAST else Heading.WEST
                    Heading.SOUTH -> if (direction == "R") Heading.WEST else Heading.EAST
                    Heading.EAST -> if (direction == "R") Heading.SOUTH else Heading.NORTH
                    Heading.WEST -> if (direction == "R") Heading.NORTH else Heading.SOUTH
                }

                position = Pair(position.first + vectors[heading]!!.first * distance.toInt(), position.second + vectors[heading]!!.second * distance.toInt())
            }
        }

        return position.first.absoluteValue + position.second.absoluteValue
    }

    fun findFirstLocationVisitedTwice(directions: String): Int {
        var heading = Heading.NORTH
        var position = Pair(0,0)
        val setOfVisitedLocations = mutableSetOf<Pair<Int,Int>>()

        directions.split(", ".toRegex()).forEach { step ->
            Regex("([RL])(\\d+)").matchEntire(step.trim())?.destructured?.let { (direction, distance) ->
                heading = when (heading) {
                    Heading.NORTH -> if (direction == "R") Heading.EAST else Heading.WEST
                    Heading.SOUTH -> if (direction == "R") Heading.WEST else Heading.EAST
                    Heading.EAST -> if (direction == "R") Heading.SOUTH else Heading.NORTH
                    Heading.WEST -> if (direction == "R") Heading.NORTH else Heading.SOUTH
                }

                for (block in 1..distance.toInt()) {
                    position = Pair(
                        position.first + vectors[heading]!!.first,
                        position.second + vectors[heading]!!.second
                    )
                    if (setOfVisitedLocations.contains(position)) {
                        return position.first.absoluteValue + position.second.absoluteValue
                    } else {
                        setOfVisitedLocations.add(position)
                    }
                }
            }
        }

        throw Exception("No position visited twice")
    }


    private enum class Heading {
        NORTH, EAST, SOUTH, WEST;
    }

    private val vectors = mapOf(
        Heading.NORTH to Pair(0,-1),
        Heading.SOUTH to Pair(0,1),
        Heading.EAST to Pair(1,0),
        Heading.WEST to Pair(-1,0)
    )

}