package days.aoc2020

import days.Day
import kotlin.math.absoluteValue

class Day12: Day(2020, 12) {
    override fun partOne(): Any {
        val ferry = Ferry()

        inputList.forEach { ferry.processInstruction(it) }

        return ferry.position.first.absoluteValue + ferry.position.second.absoluteValue
    }

    override fun partTwo(): Any {
        val ferry = FerryAndWaypoint()

        inputList.forEach { ferry.processInstruction(it) }

        return ferry.position.first.absoluteValue + ferry.position.second.absoluteValue
    }

    class FerryAndWaypoint {
        var position = Pair(0,0)
        var waypoint = Pair(10,-1)

        fun processInstruction(instruction: String) {
            when {
                instruction.startsWith("F") -> {
                    move(instruction.substring(1).toInt())
                }
                instruction.startsWith("L") -> {
                    turn("L", instruction.substring(1).toInt())
                }
                instruction.startsWith("R") -> {
                    turn("R", instruction.substring(1).toInt())
                }
                instruction.startsWith("N") -> {
                    moveWaypoint(Heading.NORTH, instruction.substring(1).toInt())
                }
                instruction.startsWith("S") -> {
                    moveWaypoint(Heading.SOUTH, instruction.substring(1).toInt())
                }
                instruction.startsWith("E") -> {
                    moveWaypoint(Heading.EAST, instruction.substring(1).toInt())
                }
                instruction.startsWith("W") -> {
                    moveWaypoint(Heading.WEST, instruction.substring(1).toInt())
                }
            }

        }

        private fun move(amount: Int) {
            position = Pair(
                    position.first + amount * waypoint.first,
                    position.second + amount * waypoint.second,
            )
        }

        private fun moveWaypoint(heading: Heading, amount: Int) {
            waypoint = Pair(
                    waypoint.first + amount * vectors[heading]!!.first,
                    waypoint.second + amount * vectors[heading]!!.second
            )
        }

        private fun turn(direction: String, amount: Int) {
            for (i in (amount - 90) downTo 0 step 90) {
                waypoint = when(direction) {
                    "R" -> {
                        Pair(
                                waypoint.second * -1,
                                waypoint.first
                        )
                    }
                    "L" -> {
                            Pair(
                                    waypoint.second,
                                    waypoint.first * -1
                            )
                        }
                    else -> waypoint
                }
            }
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

    class Ferry {
        var position = Pair(0,0)
        private var heading = Heading.EAST

        fun processInstruction(instruction: String) {
            when {
                instruction.startsWith("F") -> {
                    move(heading, instruction.substring(1).toInt())
                }
                instruction.startsWith("L") -> {
                    turn("L", instruction.substring(1).toInt())
                }
                instruction.startsWith("R") -> {
                    turn("R", instruction.substring(1).toInt())
                }
                instruction.startsWith("N") -> {
                    move(Heading.NORTH, instruction.substring(1).toInt())
                }
                instruction.startsWith("S") -> {
                    move(Heading.SOUTH, instruction.substring(1).toInt())
                }
                instruction.startsWith("E") -> {
                    move(Heading.EAST, instruction.substring(1).toInt())
                }
                instruction.startsWith("W") -> {
                    move(Heading.WEST, instruction.substring(1).toInt())
                }
            }

        }

        private fun move(heading: Heading, amount: Int) {
            position = Pair(
                    position.first + amount * vectors[heading]!!.first,
                    position.second + amount * vectors[heading]!!.second
            )
        }

        private fun turn(direction: String, amount: Int) {
            when(direction) {
                "R" -> {
                    for (i in (amount - 90) downTo 0 step 90) {
                        heading = heading.right(heading)
                    }
                }
                "L" -> {
                    for (i in (amount - 90) downTo 0 step 90) {
                        heading = heading.left(heading)
                    }
                }
            }
        }

        private enum class Heading {
            NORTH, EAST, SOUTH, WEST;

            fun right(heading: Heading): Heading {
                return when(heading) {
                    NORTH -> EAST
                    EAST -> SOUTH
                    SOUTH -> WEST
                    WEST -> NORTH
                }
            }
            fun left(heading: Heading): Heading {
                return when(heading) {
                    NORTH -> WEST
                    EAST -> NORTH
                    SOUTH -> EAST
                    WEST -> SOUTH
                }
            }
        }

        private val vectors = mapOf(
                Heading.NORTH to Pair(0,-1),
                Heading.SOUTH to Pair(0,1),
                Heading.EAST to Pair(1,0),
                Heading.WEST to Pair(-1,0)
        )
    }

}