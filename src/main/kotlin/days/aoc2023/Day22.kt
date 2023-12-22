package days.aoc2023

import days.Day
import kotlin.math.min
import util.Point3d
import util.toward

class Day22 : Day(2023, 22) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    class Brick(private val xRange: IntProgression, private val yRange: IntProgression, var zRange: IntProgression): Cloneable {

        public override fun clone(): Any {
            return Brick(xRange, yRange, zRange)
        }

        fun fall() {
            zRange = (zRange.min() - 1) .. (zRange.max() - 1)
        }

        fun canFall(otherBricks: List<Brick>): Boolean {
            return zRange.min() > 1 && supportingBricks(otherBricks).isEmpty()
        }

        fun supportingBricks(otherBricks: List<Brick>): List<Brick> {
            val supportingBricks = mutableSetOf<Brick>()

            // see if we're on the ground
            if (zRange.min() == 1) {
                return emptyList()
            }
            supportingBricks.addAll(otherBricks.filter { ((zRange.min() - 1) == it.zRange.max()) && ((xRange intersect it.xRange).isNotEmpty() && (yRange intersect it.yRange).isNotEmpty()) })

            return supportingBricks.toList()
        }

        fun isOnlySupportingBrick(otherBricks: List<Brick>): Boolean {
            return otherBricks.filter { it.zRange.min() == (zRange.max() + 1) }.any {
                val supportingBricks = it.supportingBricks(otherBricks.plus(this).minus(it))
                supportingBricks.contains(this) && supportingBricks.size == 1
            }
        }

        // a brick can be disintegrated if it is not the only supporting brick
        // for any other brick
        fun canBeDisintegrated(otherBricks: List<Brick>): Boolean {
            return !isOnlySupportingBrick(otherBricks)
        }
    }

    fun calculatePartOne(input: List<String>): Int {
        val bricks = input.map { line ->
            Regex("(\\d),(\\d),(\\d+)~(\\d),(\\d),(\\d+)").matchEntire(line)?.destructured?.let {(x1, y1, z1, x2, y2, z2) ->
                    Brick(x1.toInt() toward x2.toInt(), y1.toInt() toward y2.toInt(), z1.toInt() toward z2.toInt())
            } ?: throw IllegalStateException("input wrong")
        }.sortedBy { it.zRange.min() }

        letBricksFall(bricks)
        return bricks.count { it.canBeDisintegrated(bricks.minus(it)) }
    }

    private fun letBricksFall(bricks: List<Brick>): Int {
        var bricksFell = true

        val bricksThatFell = mutableSetOf<Brick>()
        while (bricksFell) {
            bricksFell = false
            bricks.filter { it.canFall(bricks.minus(it)) }.forEach {
                it.fall()
                bricksThatFell.add(it)
                bricksFell = true
            }
        }

        return bricksThatFell.size
    }

    fun calculatePartTwo(input: List<String>): Int {
        val bricks = input.map { line ->
            Regex("(\\d),(\\d),(\\d+)~(\\d),(\\d),(\\d+)").matchEntire(line)?.destructured?.let {(x1, y1, z1, x2, y2, z2) ->
                Brick(x1.toInt() toward x2.toInt(), y1.toInt() toward y2.toInt(), z1.toInt() toward z2.toInt())
            } ?: throw IllegalStateException("input wrong")
        }.sortedBy { it.zRange.min() }

        letBricksFall(bricks)

        var sum = 0


        bricks.forEachIndexed { i, _ ->
            val newStack = bricks.map { it.clone() as Brick }
            val bricksThatFell = letBricksFall(newStack.minus(newStack[i]))
            sum += bricksThatFell
            println("$bricksThatFell bricks fell after removing number $i, making $sum so far")
        }

        return sum
    }
}