package days.aoc2024

import days.Day
import days.aoc2022.Day9
import util.Point2d
import kotlin.math.min

class Day14 : Day(2024, 14) {
    override fun partOne(): Any {
        return calculatePartOne(inputList, Point2d(101, 103))
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList, Point2d(101, 103))
    }

    internal data class Robot(val position: Point2d, val velocity: Point2d)

    fun calculatePartOne(input: List<String>, roomSize: Point2d): Int {
        var robots = input.map { line ->
            Regex("p=(\\d+),(\\d+) v=(-)?(\\d+),(-)?(\\d+)").matchEntire(line)?.destructured?.let { (x, y, xVelSign, xVel, yVelSign, yVel) ->
                Robot(
                    Point2d(x.toInt(), y.toInt()),
                    Point2d(
                        xVel.toInt() * if ("-" == xVelSign) -1 else 1,
                        yVel.toInt() * if ("-" == yVelSign) -1 else 1
                    )
                )
            }
        }

        robots = robots.map { calculateRobotPositionAfterSeconds(it!!, 100, roomSize) }
        return calculateDangerLevel(robots, roomSize)
    }

    private fun calculateDangerLevel(robots: List<Robot>, roomSize: Point2d): Int {
        return robots.count { it.position.x < roomSize.x / 2 && it.position.y < roomSize.y / 2 } *
                robots.count { it.position.x > roomSize.x / 2 && it.position.y < roomSize.y / 2 } *
                robots.count { it.position.x < roomSize.x / 2 && it.position.y > roomSize.y / 2 } *
                robots.count { it.position.x > roomSize.x / 2 && it.position.y > roomSize.y / 2 }
    }

    private fun calculateRobotPositionAfterSeconds(robot: Robot, seconds: Int, roomSize: Point2d): Robot {
        val newPosition = robot.position + (robot.velocity * seconds)
        val returnVal = robot.copy(position = Point2d(newPosition.x.mod(roomSize.x), newPosition.y.mod(roomSize.y)))
        return returnVal
    }

    fun calculatePartTwo(input: List<String>, roomSize: Point2d): Int {
        val originalRobots = input.map { line ->
            Regex("p=(\\d+),(\\d+) v=(-)?(\\d+),(-)?(\\d+)").matchEntire(line)?.destructured?.let { (x, y, xVelSign, xVel, yVelSign, yVel) ->
                Robot(
                    Point2d(x.toInt(), y.toInt()),
                    Point2d(
                        xVel.toInt() * if ("-" == xVelSign) -1 else 1,
                        yVel.toInt() * if ("-" == yVelSign) -1 else 1
                    )
                )
            } ?: throw IllegalStateException("Bad format found: $line")
        }
        var i = 1
        while(true) {
            val robots = originalRobots.map { calculateRobotPositionAfterSeconds(it, i, roomSize) }
            val positions = robots.map { it.position }
            if (positions.any { isPartOfStraightLine(it, positions) }) {
                println("After $i seconds...")
                for (y in 0 until roomSize.y) {
                    for (x in 0 until roomSize.x) {
                        print(if (robots.map { it.position }.contains(Point2d(x, y))) "#" else " ")
                    }
                    println()
                }
                println("---------------------------------------------------------------------------------------")
                return i
            }
            i += 1
        }
    }

    private fun isPartOfStraightLine(point: Point2d, others: List<Point2d>): Boolean {
        for (x in point.x .. point.x + 10) {
            if (!others.contains(Point2d(x, point.y)))
                return false
        }
        return true
    }
}