package days.aoc2021

import days.Day

class Day2 : Day(2021, 2) {
    override fun partOne(): Any {
        return calculatePositionAndMultiply(inputList)
    }

    override fun partTwo(): Any {
        return calculateAimPositionAndMultiply(inputList)
    }

    fun calculatePositionAndMultiply(input: List<String>): Int {
        var depth = 0
        var horizontal = 0
        input.forEach { line ->
            Regex("(\\w+) ([0-9]+)").matchEntire(line)?.destructured?.let { (direction, distanceString) ->
                val distance = distanceString.toInt()
                when (direction) {
                    "forward" -> {
                        horizontal += distance
                    }
                    "up" -> {
                        depth -= distance
                        if (depth < 0) depth = 0
                    }
                    "down" -> {
                        depth += distance
                    }
                }
            }
        }

        return depth * horizontal
    }

    fun calculateAimPositionAndMultiply(input: List<String>): Int {
        var aim = 0
        var depth = 0
        var horizontal = 0
        input.forEach { line ->
            Regex("(\\w+) ([0-9]+)").matchEntire(line)?.destructured?.let { (direction, distanceString) ->
                val distance = distanceString.toInt()
                when (direction) {
                    "forward" -> {
                        horizontal += distance
                        if (aim != 0) {
                            depth += aim * distance
                        }
                    }
                    "up" -> {
                        aim -= distance
                    }
                    "down" -> {
                        aim += distance
                    }
                }
            }
        }

        return depth * horizontal
    }

}