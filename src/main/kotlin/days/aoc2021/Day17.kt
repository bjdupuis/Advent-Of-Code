package days.aoc2021

import days.Day
import java.lang.IllegalArgumentException

class Day17 : Day(2021, 17) {
    override fun partOne(): Any {
        return findHighestY(inputString)
    }

    override fun partTwo(): Any {
        return countAllPossibleTrajectories(inputString)
    }

    fun findHighestY(inputString: String): Int {
        Regex("target area: x=(\\d+)..(\\d+), y=-(\\d+)..-(\\d+)").matchEntire(inputString.trim())?.destructured?.let { (x1, x2, y1, y2) ->
            val target = Target(x1.toInt()..x2.toInt(), -y1.toInt()..-y2.toInt())

            var maxY = -1
            for (x in 1..(x1.toInt()+x2.toInt())) {
                for (y in -100..300) {
                    target.shootDrone(x, y)?.let {
                        maxY = maxOf(maxY, it)
                    }
                }
            }

            return maxY
        } ?: throw IllegalArgumentException()

        return 0
    }

    fun countAllPossibleTrajectories(inputString: String): Int {
        Regex("target area: x=(\\d+)..(\\d+), y=-(\\d+)..-(\\d+)").matchEntire(inputString.trim())?.destructured?.let { (x1, x2, y1, y2) ->
            val target = Target(x1.toInt()..x2.toInt(), -y1.toInt()..-y2.toInt())

            var count = 0
            for (x in 1..(x1.toInt()+x2.toInt() + 400)) {
                for (y in -500..500) {
                    target.shootDrone(x, y)?.let {
                        count++
                    }
                }
            }

            return count
        } ?: throw IllegalArgumentException()

        return 0
    }

    class Target(private val rangeX: IntRange, private val rangeY: IntRange) {
        fun shootDrone(x: Int, y: Int): Int? {
            var currentX = 0
            var currentY = 0
            var velocityX = x
            var velocityY = y
            var maxY = Int.MIN_VALUE
            var hit = false

            while (currentX <= rangeX.last && currentY > rangeY.first) {
                currentX += velocityX
                currentY += velocityY
                velocityX = if (velocityX > 0) velocityX - 1 else if (velocityX < 0) velocityX + 1 else 0
                velocityY--

                maxY = maxOf(maxY, currentY)
                if (rangeX.contains(currentX) && rangeY.contains(currentY)) {
                    hit = true
                }
            }

            return if (hit) maxY else null
        }
    }
}