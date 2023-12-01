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

                    map[Point2d(x, y - 1)]?.let { neighbor ->
                        position.up = neighbor
                        neighbor.down = position
                    }
                    map[Point2d(x - 1, y)]?.let { neighbor ->
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
                }
                "L" -> {
                    facing = facing.left()
                }
                else -> {
                    val amount = step.toInt()
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
                }
            }
        }

        return (1000 * (currentPosition!!.coords.y + 1)) + (4 * (currentPosition!!.coords.x + 1)) + facing.ordinal
    }

    fun calculateFinalPasswordOnCube(input: List<String>, faceSize: Int): Int {
        val map = parseCube(input, faceSize)
        var currentPosition: Position? = map[Point2d(50,0)]

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
                    val (newfacing, newPosition) = move(step.toInt(), facing, currentPosition!!)
                    facing = newfacing
                    currentPosition = newPosition
                    println("now at ${currentPosition!!.coords.x}, ${currentPosition!!.coords.y} facing ${facing.name}")
                }
            }
        }

        return (1000 * (currentPosition!!.coords.y + 1)) + (4 * (currentPosition!!.coords.x + 1)) + facing.ordinal
    }

    fun parseCube(input: List<String>, faceSize: Int): Map<Point2d, Position> {
        val map = mutableMapOf<Point2d, Position>()
        input.dropLast(2).forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '.' || c == '#') {
                    val point = Point2d(x, y)
                    val position = if (c == '#') Wall(point) else Position(point)
                    map[position.coords] = position

                    map[Point2d(x, y - 1)]?.let { neighbor ->
                        position.up = neighbor
                        neighbor.down = position
                    }
                    map[Point2d(x - 1, y)]?.let { neighbor ->
                        position.left = neighbor
                        neighbor.right = position
                    }
                }
            }
        }

        // now fix the sides
        for (i in 0 until faceSize) {
            val face1Top = map[Point2d(i + faceSize, 0)]!!
            val face1Left = map[Point2d(faceSize, (faceSize - 1) - i)]!!
            val face6Left = map[Point2d(0, (3 * faceSize + i))]!!
            val face4Left = map[Point2d(0, 2 * faceSize + i)]!!
            val face3Left = map[Point2d(faceSize, faceSize + i)]!!
            val face4Top = map[Point2d(i, 2 * faceSize)]!!
            val face2Down = map[Point2d(2 * faceSize + i, faceSize - 1)]!!
            val face3Right = map[Point2d(2 * faceSize - 1, faceSize + i)]!!
            val face5Right = map[Point2d(2 * faceSize - 1, 2 * faceSize + i)]!!
            val face5Down = map[Point2d(faceSize + i, 3 * faceSize - 1)]!!
            val face6Right = map[Point2d(faceSize - 1, 3 * faceSize + i)]!!
            val face6Down = map[Point2d(i, 4 * faceSize - 1)]!!
            val face2Up = map[Point2d(2 * faceSize + i, 0)]!!
            val face2Right = map[Point2d(3 * faceSize - 1, (faceSize - 1) - i)]!!

            face1Top.up = face6Left
            face6Left.left = face1Top
            face1Top.turn = { was, position ->
                when (position) {
                    face6Left -> Facing.DOWN
                    else -> was
                }
            }
            face6Left.turn = { was, position ->
                when(position) {
                    face1Top -> Facing.RIGHT
                    else -> was
                }
            }

            face3Left.left = face4Top
            face4Top.up = face3Left
            face3Left.turn = { was, position ->
                when(position) {
                    face4Top -> Facing.RIGHT
                    else -> was
                }
            }
            face4Top.turn = { was, position ->
                when(position) {
                    face3Left -> Facing.DOWN
                    else -> was
                }
            }

            face2Down.down = face3Right
            face3Right.right = face2Down
            face2Down.turn = { was, position ->
                when (position) {
                    face3Right -> Facing.UP
                    else -> was
                }
            }
            face3Right.turn = { was, position ->
                when (position) {
                    face2Down -> Facing.LEFT
                    else -> was
                }
            }

            face5Down.down = face6Right
            face6Right.right = face5Down
            face5Down.turn = { was, position ->
                when (position) {
                    face6Right -> Facing.UP
                    else -> was
                }
            }
            face6Right.turn = { was, position ->
                when (position) {
                    face5Down -> Facing.LEFT
                    else -> was
                }
            }

            face6Down.down = face2Up
            face2Up.up = face6Down

            face4Left.left = face1Left
            face1Left.left = face4Left
            face4Left.turn = { was, position ->
                when (position) {
                    face1Left -> Facing.RIGHT
                    else -> was
                }
            }
            face1Left.turn = { was, position ->
                when (position) {
                    face4Left -> Facing.RIGHT
                    else -> was
                }
            }

            face5Right.right = face2Right
            face2Right.right = face5Right
            face5Right.turn = { was, position ->
                when (position) {
                    face2Right -> Facing.LEFT
                    else -> was
                }
            }

            face2Right.turn = { was, position ->
                when (position) {
                    face5Right -> Facing.LEFT
                    else -> was
                }
            }
        }

        val face1 = mapOf(
            "UL" to Point2d(faceSize, 0),
            "UR" to Point2d(2 * faceSize - 1, 0),
            "LL" to Point2d(faceSize, faceSize - 1),
            "LR" to Point2d(2 * faceSize - 1, faceSize - 1),
        )
        val face2 = mapOf(
            "UL" to Point2d(2 * faceSize, 0),
            "UR" to Point2d(3 * faceSize - 1, 0),
            "LL" to Point2d(2 * faceSize, faceSize - 1),
            "LR" to Point2d(3 * faceSize - 1, faceSize - 1),
        )
        val face3 = mapOf(
            "UL" to Point2d(faceSize, faceSize),
            "UR" to Point2d(2 * faceSize - 1, faceSize),
            "LL" to Point2d(faceSize, 2 * faceSize - 1),
            "LR" to Point2d(2 * faceSize - 1, 2 * faceSize - 1),
        )
        val face4 = mapOf(
            "UL" to Point2d(0, 2 * faceSize),
            "UR" to Point2d(faceSize - 1, 2 * faceSize),
            "LL" to Point2d(0, 3 * faceSize - 1),
            "LR" to Point2d(faceSize - 1, 3 * faceSize - 1),
        )
        val face5 = mapOf(
            "UL" to Point2d(faceSize, 2 * faceSize),
            "UR" to Point2d(2 * faceSize - 1, 2 * faceSize),
            "LL" to Point2d(faceSize, 3 * faceSize - 1),
            "LR" to Point2d(2 * faceSize - 1, 3 * faceSize - 1),
        )
        val face6 = mapOf(
            "UL" to Point2d(0, 3 * faceSize),
            "UR" to Point2d(faceSize - 1, 3 * faceSize),
            "LL" to Point2d(0, 4 * faceSize - 1),
            "LR" to Point2d(faceSize - 1, 4 * faceSize - 1),
        )
        map[face1["UL"]]!!.turn = { was, position ->
            when (position.coords) {
                face4["LL"] -> Facing.RIGHT
                face6["UL"] -> Facing.DOWN
                else -> was
            }
        }
        map[face1["UR"]]!!.turn = { was, position ->
            when (position.coords) {
                face2["UL"] -> Facing.LEFT
                face6["LL"] -> Facing.DOWN
                else -> was
            }
        }
        map[face1["LL"]]!!.turn = { was, position ->
            when (position.coords) {
                face4["UL"] -> Facing.RIGHT
                face3["UL"] -> Facing.UP
                else -> was
            }
        }
        map[face1["LR"]]!!.turn = { was, position ->
            when (position.coords) {
                face2["LL"] -> Facing.LEFT
                face3["UR"] -> Facing.UP
                else -> was
            }
        }

        map[face2["UL"]]!!.turn = { was, position ->
            when (position.coords) {
                face1["UR"] -> Facing.RIGHT
                face6["LL"] -> Facing.DOWN
                else -> was
            }
        }
        map[face2["UR"]]!!.turn = { was, position ->
            when (position.coords) {
                face5["LR"] -> Facing.LEFT
                face6["LR"] -> Facing.DOWN
                else -> was
            }
        }
        map[face2["LL"]]!!.turn = { was, position ->
            when (position.coords) {
                face1["LR"] -> Facing.RIGHT
                face3["UR"] -> Facing.UP
                else -> was
            }
        }
        map[face2["LR"]]!!.turn = { was, position ->
            when (position.coords) {
                face3["LR"] -> Facing.UP
                face5["UR"] -> Facing.LEFT
                else -> was
            }
        }

        map[face3["UL"]]!!.turn = { was, position ->
            when (position.coords) {
                face1["LL"] -> Facing.DOWN
                face4["UL"] -> Facing.RIGHT
                else -> was
            }
        }
        map[face3["UR"]]!!.turn = { was, position ->
            when (position.coords) {
                face1["LR"] -> Facing.DOWN
                face2["LL"] -> Facing.LEFT
                else -> was
            }
        }
        map[face3["LL"]]!!.turn = { was, position ->
            when (position.coords) {
                face4["UR"] -> Facing.RIGHT
                face5["UL"] -> Facing.UP
                else -> was
            }
        }
        map[face3["LR"]]!!.turn = { was, position ->
            when (position.coords) {
                face5["UR"] -> Facing.UP
                face2["LR"] -> Facing.LEFT
                else -> was
            }
        }

        map[face4["UL"]]!!.turn = { was, position ->
            when (position.coords) {
                face3["UL"] -> Facing.DOWN
                face1["LL"] -> Facing.RIGHT
                else -> was
            }
        }
        map[face4["UR"]]!!.turn = { was, position ->
            when (position.coords) {
                face3["LL"] -> Facing.DOWN
                face5["UL"] -> Facing.LEFT
                else -> was
            }
        }
        map[face4["LL"]]!!.turn = { was, position ->
            when (position.coords) {
                face1["UL"] -> Facing.RIGHT
                face6["UL"] -> Facing.UP
                else -> was
            }
        }
        map[face4["LR"]]!!.turn = { was, position ->
            when (position.coords) {
                face6["UR"] -> Facing.UP
                face5["LL"] -> Facing.LEFT
                else -> was
            }
        }

        map[face5["UL"]]!!.turn = { was, position ->
            when (position.coords) {
                face3["LL"] -> Facing.DOWN
                face4["UR"] -> Facing.RIGHT
                else -> was
            }
        }
        map[face5["UR"]]!!.turn = { was, position ->
            when(position.coords) {
                face3["LR"] -> Facing.DOWN
                face2["LR"] -> Facing.LEFT
                else -> was
            }
        }
        map[face5["LL"]]!!.turn = { was, position ->
            when (position.coords) {
                face4["LR"] -> Facing.RIGHT
                face6["UR"] -> Facing.UP
                else -> was
            }
        }
        map[face5["LR"]]!!.turn = { was, position ->
            when (position.coords) {
                face6["LR"] -> Facing.UP
                face2["UR"] -> Facing.LEFT
                else -> was
            }
        }

        map[face6["UL"]]!!.turn = { was, position ->
            when (position.coords) {
                face4["LL"] -> Facing.DOWN
                face1["UL"] -> Facing.RIGHT
                else -> was
            }
        }
        map[face6["UR"]]!!.turn = { was, position ->
            when (position.coords) {
                face4["LR"] -> Facing.DOWN
                face5["LL"] -> Facing.LEFT
                else -> was
            }
        }
        map[face6["LL"]]!!.turn = { was, position ->
            when (position.coords) {
                face2["UL"] -> Facing.UP
                face1["UR"] -> Facing.RIGHT
                else -> was
            }
        }
        map[face6["LR"]]!!.turn = { was, position ->
            when (position.coords) {
                face2["UR"] -> Facing.UP
                face5["LR"] -> Facing.LEFT
                else -> was
            }
        }

        return map
    }

    fun move(amount: Int, facing: Facing, currentPosition: Position): Pair<Facing, Position> {
        print("Moving $amount steps... ")
        var newFacing = facing
        var newPosition = currentPosition
        for (i in 1..amount) {
            when (facing) {
                Facing.RIGHT -> {
                    if (newPosition.right !is Wall) {
                        newFacing = newPosition.right!!.turn?.invoke(newFacing, newPosition) ?: newFacing
                        newPosition = newPosition.right!!
                    }
                }
                Facing.LEFT -> {
                    if (newPosition.left !is Wall) {
                        newFacing = newPosition.left!!.turn?.invoke(newFacing, newPosition) ?: newFacing
                        newPosition = newPosition.left!!
                    }
                }
                Facing.UP -> {
                    if (newPosition.up !is Wall) {
                        newFacing = newPosition.up!!.turn?.invoke(newFacing, newPosition) ?: newFacing
                        newPosition = newPosition.up!!
                    }
                }
                Facing.DOWN -> {
                    if (newPosition.down !is Wall) {
                        newFacing = newPosition.down!!.turn?.invoke(newFacing, newPosition) ?: newFacing
                        newPosition = newPosition.down!!
                    }
                }
            }
        }

        return Pair(newFacing, newPosition)
    }

    open class Position(val coords: Point2d) {
        var right: Position? = null
        var left: Position? = null
        var up: Position? = null
        var down: Position? = null
        var turn: ((Facing, Position) -> Facing)? = null
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