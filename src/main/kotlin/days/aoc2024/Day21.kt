package days.aoc2024

import days.Day
import util.Pathfinding
import util.Pathfinding.NeighborFilter

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
        private val pathsToKey: Map<Char, Map<Char, List<List<Char>>>>

        fun pathsTo(destination: Char): List<List<Char>> {
            val moves = (pathsToKey[currentLocation]!![destination] ?: emptyList())
            currentLocation = destination
            return moves
        }

        init {
            pathsToKey = initializePaths()
        }

        abstract fun initializePaths(): Map<Char, Map<Char, List<List<Char>>>>
    }

    class DirectionalKeypadEntryRobot : KeypadEntryRobot() {

        override fun initializePaths(): Map<Char, Map<Char, List<List<Char>>>> {
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
            val paths = mutableMapOf<Char, Map<Char, List<List<Char>>>>()
            val pathfinding = Pathfinding<Pair<Char,Char>>()
            directionalMoves.keys.forEach { start ->
                val map = mutableMapOf<Char,List<List<Char>>>()
                directionalMoves.keys.minus(start).forEach { end ->
                    val path = pathfinding.findAllPaths(
                        Pair(start,' '),
                        { current -> directionalMoves[current.first]!! },
                        { _, _ -> true },
                        { current -> current.first == end }
                    )
                    map[end] = path.map { it.map { it.second } + 'A' }
                }
                paths[start] = map
            }
            return paths
        }

    }

    class NumericKeypadEntryRobot: KeypadEntryRobot() {

        override fun initializePaths(): Map<Char, Map<Char, List<List<Char>>>> {
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
            val paths = mutableMapOf<Char, Map<Char, List<List<Char>>>>()
            val pathfinding = Pathfinding<Char>()
            numericMoves.keys.forEach { start ->
                val map = mutableMapOf<Char,List<List<Char>>>()
                numericMoves.keys.minus(start).forEach { end ->
                    val path = pathfinding.findAllPaths(
                        start,
                        { current -> numericMoves[current]!!.map { it.second } },
                        { _, _ -> true },
                        { current -> current.first == end }
                    )
                    map[end] = path.map { it.map { it.second } + 'A' }
                }
                paths[start] = map
            }
            return paths
        }
    }

    fun findAllPaths(
        start: Pair<Char,Char>,
        neighborIterator: (Pair<Char,Char>) -> List<Pair<Char,Char>>,
        terminationCondition: (Pair<Char,Char>) -> Boolean
    ): List<List<Char>> {
        val completedPaths = mutableListOf<List<List<Char>>>()
        val potentialPath = ArrayDeque<Pair<Pair<Char,Char>, List<Pair<Char,Char>>>>()
        potentialPath.addFirst(Pair(start, listOf()))

        while (potentialPath.isNotEmpty()) {
            val current = potentialPath.removeFirst()
            if (terminationCondition(current.first)) {
                completedPaths.add(current.second.map { it })
            } else {
                neighborIterator(current.first)
                    .filter { !current.second.any { it.first == current.first.first } }.forEach {
                        potentialPath.addFirst(it to current.second.plus(current.first))
                    }
            }
        }

        return completedPaths
    }


    fun calculatePartOne(input: List<String>): Int {
        val numericKeypadEntryRobot = NumericKeypadEntryRobot()
        val directionalKeypadRobot1 = DirectionalKeypadEntryRobot()
        val directionalKeypadRobot2 = DirectionalKeypadEntryRobot()

        return input.sumOf { line ->
            val paths = mutableListOf<List<List<Char>>>()
            paths.addAll(line.flatMap { keypadEntry ->
                val numericPaths = numericKeypadEntryRobot.pathsTo(keypadEntry)
                numericPaths.flatMap { numericPath ->
                    numericPath.flatMap { numericMove ->
                        val r1Paths = directionalKeypadRobot1.pathsTo(numericMove)
                        r1Paths.flatMap { r1Path ->
                            r1Path.map { r1Move ->
                                directionalKeypadRobot2.pathsTo(r1Move)
                            }
                        }
                    }
                }
            })

            paths.minOf { it.size } * line.dropLast(1).trimStart('0').toInt()
        }
    }

    fun calculatePartTwo(input: List<String>): Int {
        return 0
    }
}