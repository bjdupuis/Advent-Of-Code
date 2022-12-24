package days.aoc2022

import days.Day
import util.Point2d

class Day22 : Day(2022, 22) {
    override fun partOne(): Any {
        return calculateFinalPassword(inputList)
    }

    override fun partTwo(): Any {
        return calculateFinalPasswordOnCube(inputList, 50)
    }

    fun calculateFinalPassword(input: List<String>): Int {
        val map = mutableMapOf<Point2d, Position>()
        val tops = mutableMapOf<Int, Position>()
        val lefts = mutableMapOf<Int, Position>()
        var currentPosition: Position? = null
        input.dropLast(2).forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '.' || c == '#') {
                    val point = Point2d(x, y)
                    val position = if (c == '#') Wall(point) else Position(point)
                    if (currentPosition == null && position !is Wall) {
                        currentPosition = position
                    }
                    map[position.coords] = position
                    if (!tops.containsKey(x)) tops[x] = position
                    if (!lefts.containsKey(y)) lefts[y] = position

                    var neighbor = map[Point2d(x, y - 1)]
                    if (neighbor != null) {
                        position.up = neighbor
                        neighbor.down = position
                    }
                    neighbor = map[Point2d(x - 1, y)]
                    if (neighbor != null) {
                        position.left = neighbor
                        neighbor.right = position
                    }

                    if (x + 1 !in line.indices) {
                        if (lefts[y] == null) throw IllegalStateException("Should have a left for this")
                        position.right = lefts[y]
                        lefts[y]?.left = position
                        lefts.remove(y)
                    }

                    if (y + 1 !in input.indices || x !in input[y + 1].indices || input[y + 1][x] == ' ') {
                        if (tops[x] == null) throw IllegalStateException("Should have a top for this")
                        position.down = tops[x]
                        tops[x]?.up = position
                        tops.remove(x)
                    }
                }
            }
        }

        var facing = Facing.RIGHT
        Regex("[RL]|\\d+").findAll(input.last()).map { it.groupValues.first() }.toList().forEach { step: String ->
            when (step) {
                "R" -> {
                    facing = facing.right()
                    println("Now facing ${facing.name}")
                }
                "L" -> {
                    facing = facing.left()
                    println("Now facing ${facing.name}")
                }
                else -> {
                    val amount = step.toInt()
                    println("Moving $amount steps")
                    for (i in 1..amount) {
                        when (facing) {
                            Facing.RIGHT -> {
                                if (currentPosition!!.right !is Wall) {
                                    currentPosition = currentPosition!!.right
                                }
                            }
                            Facing.LEFT -> {
                                if (currentPosition!!.left !is Wall) {
                                    currentPosition = currentPosition!!.left
                                }
                            }
                            Facing.UP -> {
                                if (currentPosition!!.up !is Wall) {
                                    currentPosition = currentPosition!!.up
                                }
                            }
                            Facing.DOWN -> {
                                if (currentPosition!!.down !is Wall) {
                                    currentPosition = currentPosition!!.down
                                }
                            }
                        }
                    }

                    println("Now at ${currentPosition!!.coords.x}, ${currentPosition!!.coords.y}")
                }
            }
        }

        return (1000 * (currentPosition!!.coords.y + 1)) + (4 * (currentPosition!!.coords.x + 1)) + facing.ordinal
    }

    // The cube shape is different than the sample.
    fun calculateFinalPasswordOnCube(input: List<String>, faceSize: Int): Int {
        val map = mutableMapOf<Point2d, Position>()
        var currentPosition: Position? = null
        input.dropLast(2).forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '.' || c == '#') {
                    val point = Point2d(x, y)
                    val position = if (c == '#') Wall(point) else Position(point)
                    if (currentPosition == null && position !is Wall) {
                        currentPosition = position
                    }
                    map[position.coords] = position

                    var neighbor = map[Point2d(x, y - 1)]
                    if (neighbor != null) {
                        position.up = neighbor
                        neighbor.down = position
                    }
                    neighbor = map[Point2d(x - 1, y)]
                    if (neighbor != null) {
                        position.left = neighbor
                        neighbor.right = position
                    }
                }
            }
        }

        // now fix the sides
        for (i in 0 until faceSize) {
            val face1Top = map[Point2d(i + faceSize * 2, 0)]!!
            val face2Top = map[Point2d((faceSize - 1) - i, faceSize)]!!
            face1Top.up = face2Top
            face2Top.up = face1Top

            face1Top.turn = { was -> when (was) { Facing.UP -> Facing.DOWN else -> was } }
            face2Top.turn = { was -> when (was) { Facing.UP -> Facing.DOWN else -> was } }

            val face1Left = map[Point2d(2 * faceSize, (faceSize - 1) - i)]!!
            val face3Top = map[Point2d((2 * faceSize - 1) - i, faceSize)]!!
            face1Left.left = face3Top
            face3Top.up = face1Left

            face1Left.turn = { was -> when (was) { Facing.LEFT -> Facing.DOWN else -> was } }
            face3Top.turn = { was -> when (was) { Facing.UP -> Facing.RIGHT else -> was } }

            val face1Right = map[Point2d(faceSize * 3 - 1, (faceSize - 1) - i)]!!
            val face6Right = map[Point2d(faceSize * 4 - 1, faceSize * 2 + i)]!!
            face1Right.right = face6Right
            face6Right.right = face1Right

            face1Right.turn = { was -> when (was) { Facing.RIGHT -> Facing.LEFT else -> was } }
            face6Right.turn = { was -> when (was) { Facing.RIGHT -> Facing.LEFT else -> was } }

            val face4Right = map[Point2d(faceSize * 3 - 1, (2 * faceSize - 1) - i)]!!
            val face6Top = map[Point2d(faceSize * 3 + i, 2 * faceSize)]!!
            face4Right.right = face6Top
            face6Top.up = face4Right

            face4Right.turn = { was -> when (was) { Facing.RIGHT -> Facing.DOWN else -> was } }
            face6Top.turn = { was -> when (was) { Facing.UP -> Facing.LEFT else -> was } }

            val face2Down = map[Point2d(i, 2 * faceSize - 1)]!!
            val face5Down = map[Point2d((3 * faceSize - 1) - i, 3 * faceSize - 1)]!!
            face2Down.down = face5Down
            face5Down.down = face2Down

            face2Down.turn = { was -> when (was) { Facing.DOWN -> Facing.UP else -> was } }
            face5Down.turn = { was -> when (was) { Facing.DOWN -> Facing.UP else -> was } }

            val face3Down = map[Point2d(faceSize + i, 2 * faceSize - 1)]!!
            val face5Left = map[Point2d(2 * faceSize, (3 * faceSize - 1) - i)]!!
            face3Down.down = face5Left
            face5Left.left = face3Down

            face3Down.turn = { was -> when (was) { Facing.DOWN -> Facing.RIGHT else -> was } }
            face5Left.turn = { was -> when (was) { Facing.LEFT -> Facing.UP else -> was } }

            val face2Left = map[Point2d(0, (2 * faceSize - 1) - i)]!!
            val face6Down = map[Point2d(3 * faceSize + i, 3 * faceSize - 1)]!!
            face2Left.left = face6Down
            face6Down.down = face2Left

            face2Left.turn = { was -> when (was) { Facing.LEFT -> Facing.UP else -> was } }
            face6Down.turn = { was -> when (was) { Facing.DOWN -> Facing.RIGHT else -> was } }
        }

        var facing = Facing.RIGHT
        Regex("[RL]|\\d+").findAll(input.last()).map { it.groupValues.first() }.toList().forEach { step: String ->
            when (step) {
                "R" -> {
                    facing = facing.right()
                    println("Now facing ${facing.name}")
                }
                "L" -> {
                    facing = facing.left()
                    println("Now facing ${facing.name}")
                }
                else -> {
                    val amount = step.toInt()
                    println("Moving $amount steps")
                    for (i in 1..amount) {
                        val newFacing = currentPosition!!.turn?.invoke(facing) ?: facing
                        var moved = false
                        when (facing) {
                            Facing.RIGHT -> {
                                if (currentPosition!!.right !is Wall) {
                                    currentPosition = currentPosition!!.right
                                    moved = true
                                }
                            }
                            Facing.LEFT -> {
                                if (currentPosition!!.left !is Wall) {
                                    currentPosition = currentPosition!!.left
                                    moved = true
                                }
                            }
                            Facing.UP -> {
                                if (currentPosition!!.up !is Wall) {
                                    currentPosition = currentPosition!!.up
                                    moved = true
                                }
                            }
                            Facing.DOWN -> {
                                if (currentPosition!!.down !is Wall) {
                                    currentPosition = currentPosition!!.down
                                    moved = true
                                }
                            }
                        }
                        if (moved) {
                            facing = newFacing
                        }
                    }

                    println("Now at ${currentPosition!!.coords.x}, ${currentPosition!!.coords.y} facing ${facing.name}")
                }
            }
        }

        return (1000 * (currentPosition!!.coords.y + 1)) + (4 * (currentPosition!!.coords.x + 1)) + facing.ordinal
    }

    open class Position(val coords: Point2d) {
        var right: Position? = null
        var left: Position? = null
        var up: Position? = null
        var down: Position? = null
        var turn: ((Facing) -> Facing)? = null
    }

    class Wall(coords: Point2d): Position(coords)

    enum class Facing {
        RIGHT, DOWN, LEFT, UP;

        fun right(): Facing {
            return when(this) {
                UP -> RIGHT
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> UP
            }
        }
        fun left(): Facing {
            return when(this) {
                UP -> LEFT
                RIGHT -> UP
                DOWN -> RIGHT
                LEFT -> DOWN
            }
        }
    }

}