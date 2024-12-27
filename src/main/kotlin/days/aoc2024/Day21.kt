package days.aoc2024

import days.Day
import util.Pathfinding

class Day21 : Day(2024, 21) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    /*
+---+---+---+
| 7 | 8 | 9 |
+---+---+---+
| 4 | 5 | 6 |
+---+---+---+
| 1 | 2 | 3 |
+---+---+---+
    | 0 | A |
    +---+---+

    +---+---+
    | ^ | A |
+---+---+---+
| < | v | > |
+---+---+---+
     */

    abstract class KeypadEntryRobot {
        private var currentLocation = 'A'
        private val shortestPathsToKey: Map<Char, Map<Char, List<Char>>>

        fun moveArmTo(destination: Char): List<Char> {
            val moves = (shortestPathsToKey[currentLocation]!![destination] ?: emptyList()) + 'A'
            currentLocation = destination
            return moves
        }

        init {
            shortestPathsToKey = initializePaths()
        }

        abstract fun initializePaths(): Map<Char, Map<Char, List<Char>>>
    }

    class DirectionalKeypadEntryRobot : KeypadEntryRobot() {

        override fun initializePaths(): Map<Char, Map<Char, List<Char>>> {
            val directionalMoves = mapOf(
                'A' to listOf(
                    '^' to '<',
                    '>' to 'v'
                ),
                '^' to listOf(
                    'A' to '>',
                    'v' to 'v'
                ),
                '<' to listOf(
                    'v' to '>'
                ),
                'v' to listOf(
                    '<' to '<',
                    '^' to '^',
                    '>' to '>'
                ),
                '>' to listOf(
                    'A' to '^',
                    'v' to '<'
                )
            )
            val paths = mutableMapOf<Char, Map<Char, List<Char>>>()
            val pathfinding = Pathfinding<Pair<Char,Char>>()
            directionalMoves.keys.forEach { start ->
                val map = mutableMapOf<Char,List<Char>>()
                directionalMoves.keys.minus(start).forEach { end ->
                    val path = pathfinding.dijkstraShortestPath(
                        Pair(start,' '),
                        { current -> directionalMoves[current.first]!! },
                        { _, _ -> true },
                        { current, neighbor ->
                            when (neighbor.second) {
                                '<' -> if (current.second == neighbor.second) 1 else 5
                                'v' -> if (current.second == neighbor.second) 1 else 50
                                '^' -> if (current.second == neighbor.second) 1 else 75
                                '>' -> if (current.second == neighbor.second) 1 else 100
                                else -> throw IllegalStateException("unrecognized direction: neighbor.second")
                            }
                        },
                        { current -> current.first == end }
                    )
                    map[end] = path.first.drop(1).map { it.second }
                }
                paths[start] = map
            }
            return paths
        }

    }

    class NumericKeypadEntryRobot: KeypadEntryRobot() {

        override fun initializePaths(): Map<Char, Map<Char, List<Char>>> {
            val numericMoves = mapOf(
                'A' to listOf(
                    '0' to '<',
                    '3' to '^'
                ),
                '0' to listOf(
                    'A' to '>',
                    '2' to '^'
                ),
                '1' to listOf(
                    '2' to '>',
                    '4' to '^'
                ),
                '2' to listOf(
                    '0' to 'v',
                    '1' to '<',
                    '5' to '^',
                    '3' to '>'
                ),
                '3' to listOf(
                    'A' to 'v',
                    '2' to '<',
                    '6' to '^'
                ),
                '4' to listOf(
                    '1' to 'v',
                    '5' to '>',
                    '7' to '^'
                ),
                '5' to listOf(
                    '2' to 'v',
                    '4' to '<',
                    '6' to '>',
                    '8' to '^'
                ),
                '6' to listOf(
                    '3' to 'v',
                    '5' to '<',
                    '9' to '^'
                ),
                '7' to listOf(
                    '4' to 'v',
                    '8' to '>'
                ),
                '8' to listOf(
                    '5' to 'v',
                    '7' to '<',
                    '9' to '>'
                ),
                '9' to listOf(
                    '6' to 'v',
                    '8' to '<'
                ),
            )
            val paths = mutableMapOf<Char, Map<Char, List<Char>>>()
            val pathfinding = Pathfinding<Pair<Char,Char>>()
            numericMoves.keys.forEach { start ->
                val map = mutableMapOf<Char,List<Char>>()
                numericMoves.keys.minus(start).forEach { end ->
                    val path = pathfinding.dijkstraShortestPath(
                        Pair(start,' '),
                        { current -> numericMoves[current.first]!! },
                        { _, _ -> true },
                        { current, neighbor ->
                            when (neighbor.second) {
                                '<' -> if (current.second == neighbor.second) 1 else 5
                                'v' -> if (current.second == neighbor.second) 1 else 50
                                '^' -> if (current.second == neighbor.second) 1 else 75
                                '>' -> if (current.second == neighbor.second) 1 else 100
                                else -> throw IllegalStateException("unrecognized direction: neighbor.second")
                            }
                        },
                        { current -> current.first == end }
                    )
                    map[end] = path.first.drop(1).map { it.second }
                }
                paths[start] = map
            }
            return paths
        }
    }

    fun calculatePartOne(input: List<String>): Int {
        val numericKeypadEntryRobot = NumericKeypadEntryRobot()
        val directionalKeypadRobot1 = DirectionalKeypadEntryRobot()
        val directionalKeypadRobot2 = DirectionalKeypadEntryRobot()

        return input.sumOf { line ->
            val numericRobotMoves = line.map { keypadEntry ->
                numericKeypadEntryRobot.moveArmTo(keypadEntry)
            }.flatten()
            val r1Moves = numericRobotMoves.map { r1Entry ->
                directionalKeypadRobot1.moveArmTo(r1Entry)
            }.flatten()
            val r2Moves = r1Moves.map { r2Entry ->
                directionalKeypadRobot2.moveArmTo(r2Entry)
            }.flatten()

            println("$line -> ${r2Moves.joinToString("")}")

            r2Moves.size * line.dropLast(1).trimStart('0').toInt()
        }
    }

    fun calculatePartTwo(input: List<String>): Int {
        return 0
    }
}