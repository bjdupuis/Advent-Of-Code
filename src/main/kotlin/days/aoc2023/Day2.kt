package days.aoc2023

import days.Day
import java.lang.Integer.min
import java.util.IllegalFormatException
import kotlin.math.max

class Day2 : Day(2023, 2) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        var possibleGameSum = 0
        input.map { line ->
            val game = GamePartOne(12, 13, 14, line)
            if (game.isPossible) {
                possibleGameSum += game.gameNumber
            }
        }

        return possibleGameSum
    }

    internal class GamePartOne(val maxRed: Int, val maxGreen: Int, val maxBlue: Int, gameInput: String) {
        val gameNumber: Int
        var isPossible: Boolean = true

        init {
            gameNumber = Regex("Game (\\d*):").find(gameInput, 0)?.destructured?.let { (number) -> number.toInt() } ?: throw IllegalStateException("Format unexpected")

            val setsOfCubes = gameInput.dropWhile { it != ':' }.drop(1).split(';')
            setsOfCubes.forEach { set ->
                set.trim().split(',').forEach { cubesDrawn ->
                    Regex("(\\d*) (red|green|blue)").find(cubesDrawn.trim())?.destructured?.let { (cubes, color) ->
                        when (color) {
                            "red" -> if (cubes.toInt() > maxRed) {
                                isPossible = false
                                return@forEach
                            }
                            "green" -> if (cubes.toInt() > maxGreen) {
                                isPossible = false
                                return@forEach
                            }
                            "blue" -> if (cubes.toInt() > maxBlue) {
                                isPossible = false
                                return@forEach
                            }
                        }
                    }
                }
            }
        }
    }

    fun calculatePartTwo(input: List<String>): Int {
        return input.sumOf { line -> GamePartTwo(line).power }
    }

    internal class GamePartTwo(gameInput: String) {

        val power: Int

        init {
            val setsOfCubes = gameInput.dropWhile { it != ':' }.drop(1).split(';')
            var minRed = 0
            var minGreen = 0
            var minBlue = 0
            setsOfCubes.forEach { set ->
                set.trim().split(',').forEach { cubesDrawn ->
                    Regex("(\\d*) (red|green|blue)").find(cubesDrawn.trim())?.destructured?.let { (cubes, color) ->
                        when (color) {
                            "red" -> minRed = max(minRed, cubes.toInt())
                            "blue" -> minBlue = max(minBlue, cubes.toInt())
                            "green" -> minGreen = max(minGreen, cubes.toInt())
                        }
                    }
                }
            }
            power = minRed * minGreen * minBlue
        }
    }
}