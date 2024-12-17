package days.aoc2024

import days.Day
import util.Pathfinding
import util.Point2d
import util.Point2dl

class Day13 : Day(2024, 13) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val machines = input.chunked(4).map { machineDescription ->
            var a = Point2d(0,0)
            var b = Point2d(0,0)
            var prize = Point2d(0,0)
            Regex("Button A: X\\+(\\d+), Y\\+(\\d+)").matchEntire(machineDescription[0])?.destructured?.let { (x, y) ->
                a = Point2d(x.toInt(), y.toInt())
            }
            Regex("Button B: X\\+(\\d+), Y\\+(\\d+)").matchEntire(machineDescription[1])?.destructured?.let { (x, y) ->
                b = Point2d(x.toInt(), y.toInt())
            }
            Regex("Prize: X=(\\d+), Y=(\\d+)").matchEntire(machineDescription[2])?.destructured?.let { (x, y) ->
                prize = Point2d(x.toInt(), y.toInt())
            }
            Machine(a, b, prize)
        }

        val fewestTokens = machines.map { machine ->
            Pathfinding<Point2d>().dijkstraShortestPathCost(
                Point2d(0, 0),
                { current -> listOf(current + machine.buttonADelta, current + machine.buttonBDelta) },
                { _, neighbor -> neighbor.x <= machine.prizeLocation.x && neighbor.y <= machine.prizeLocation.y },
                { current, neighbor -> if (neighbor - current == machine.buttonADelta) 3 else 1 },
                { neighbor -> neighbor == machine.prizeLocation }
            )
        }
        return fewestTokens.filter { it < Int.MAX_VALUE }.sum()
    }

    internal data class Machine(val buttonADelta: Point2d, val buttonBDelta: Point2d, val prizeLocation: Point2d)

    internal data class Machinel(val buttonADelta: Point2d, val buttonBDelta: Point2d, val prizeLocation: Point2dl)

    fun calculatePartTwo(input: List<String>): Long {
        val machines = input.chunked(4).map { machineDescription ->
            var a = Point2d(0,0)
            var b = Point2d(0,0)
            var prize = Point2dl(0,0)
            Regex("Button A: X\\+(\\d+), Y\\+(\\d+)").matchEntire(machineDescription[0])?.destructured?.let { (x, y) ->
                a = Point2d(x.toInt(), y.toInt())
            }
            Regex("Button B: X\\+(\\d+), Y\\+(\\d+)").matchEntire(machineDescription[1])?.destructured?.let { (x, y) ->
                b = Point2d(x.toInt(), y.toInt())
            }
            Regex("Prize: X=(\\d+), Y=(\\d+)").matchEntire(machineDescription[2])?.destructured?.let { (x, y) ->
                prize = Point2dl(x.toLong() + 10000000000000L, y.toLong() + 10000000000000L)
            }
            Machinel(a, b, prize)
        }

        val fewestTokens = machines.map { machine ->
            with(machine) {
                val a = (prizeLocation.x * buttonBDelta.y - prizeLocation.y * buttonBDelta.x) / (buttonADelta.x * buttonBDelta.y - buttonADelta.y * buttonBDelta.x)
                val b = (prizeLocation.y * buttonADelta.x - prizeLocation.x * buttonADelta.y) / (buttonADelta.x * buttonBDelta.y - buttonADelta.y * buttonBDelta.x)
                //println("$a, $b")
                if (prizeLocation == Point2dl((a * buttonADelta.x + b * buttonBDelta.x), (a * buttonADelta.y + b * buttonBDelta.y))) {
                    a * 3 + b
                } else {
                    -1
                }
            }
        }
        //println("$fewestTokens")
        return fewestTokens.filter { it > 0 }.sum()
    }
}

