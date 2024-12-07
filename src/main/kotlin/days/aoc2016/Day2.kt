package days.aoc2016

import days.Day
import util.CharArray2d

class Day2 : Day(2016, 2) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val map = CharArray2d(listOf("123","456","789"))
        var currentLocation = map.findFirst('5')!!
        return input.joinToString(separator = "") { instructions ->
            instructions.forEach { instruction ->
                currentLocation = when (instruction) {
                    'U' -> if (currentLocation.northernNeighbor.isWithin(map)) currentLocation.northernNeighbor else currentLocation
                    'D' -> if (currentLocation.southernNeighbor.isWithin(map)) currentLocation.southernNeighbor else currentLocation
                    'L' -> if (currentLocation.westernNeighbor.isWithin(map)) currentLocation.westernNeighbor else currentLocation
                    'R' -> if (currentLocation.easternNeighbor.isWithin(map)) currentLocation.easternNeighbor else currentLocation
                    else -> throw IllegalStateException("Instruction wasn't one of UDLR")
                }
            }
            map[currentLocation].toString()
        }.toInt()
    }

    fun calculatePartTwo(input: List<String>): String {
        val map = CharArray2d(listOf("..1..",".234.","56789",".ABC.","..D.."))
        var currentLocation = map.findFirst('5')!!
        return input.joinToString(separator = "") { instructions ->
            instructions.forEach { instruction ->
                currentLocation = when (instruction) {
                    'U' -> if (currentLocation.northernNeighbor.isWithin(map) && map[currentLocation.northernNeighbor] != '.') currentLocation.northernNeighbor else currentLocation
                    'D' -> if (currentLocation.southernNeighbor.isWithin(map) && map[currentLocation.southernNeighbor] != '.') currentLocation.southernNeighbor else currentLocation
                    'L' -> if (currentLocation.westernNeighbor.isWithin(map) && map[currentLocation.westernNeighbor] != '.') currentLocation.westernNeighbor else currentLocation
                    'R' -> if (currentLocation.easternNeighbor.isWithin(map) && map[currentLocation.easternNeighbor] != '.') currentLocation.easternNeighbor else currentLocation
                    else -> throw IllegalStateException("Instruction wasn't one of UDLR")
                }
            }
            map[currentLocation].toString()
        }
    }
}