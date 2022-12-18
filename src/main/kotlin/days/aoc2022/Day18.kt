package days.aoc2022

import days.Day
import util.Point3d
import kotlin.math.max
import kotlin.math.min

class Day18 : Day(2022, 18) {
    override fun partOne(): Any {
        return countExposedSidesOfCubes(inputList)
    }

    override fun partTwo(): Any {
        return countExposedExteriorSidesOfCubes(inputList)
    }

    fun countExposedSidesOfCubes(input: List<String>): Int {
        val cubes = input.map { line ->
            line.split(",").map { it.toInt() }.let {
                Point3d(it[0], it[1], it[2])
            }
        }

        return cubes.sumOf { cube ->
            6 - cubes.minus(cube).count { other -> cube.distanceTo(other) == 1 }
        }
    }

    fun countExposedExteriorSidesOfCubes(input: List<String>) : Int {
        var min = Point3d(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
        var max = Point3d(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
        val cubes = input.map { line ->
            line.split(",").map { it.toInt() }.let {
                min = min.copy(x = min(min.x, it[0]), y = min(min.y, it[1]), z = min(min.z, it[2]))
                max = max.copy(x = max(max.x, it[0]), y = max(max.y, it[1]), z = max(max.z, it[2]))
                Point3d(it[0], it[1], it[2])
            }
        }

        val exterior = mutableSetOf<Point3d>()
        val queue = mutableListOf<Point3d>()
        queue.add(min)
        while (queue.isNotEmpty()) {
            val location = queue.removeFirst()
            exterior.add(location)
            val neighbors = location.neighbors(min, max)
            queue.addAll(neighbors.filter { it !in exterior && it !in cubes && it !in queue})
        }

        return cubes.sumOf { cube ->
            cube.neighbors(min, max).count { it in exterior }
        }
    }
}

fun Point3d.neighbors(min: Point3d, max: Point3d): List<Point3d> {
    val result = mutableListOf<Point3d>()

    if (x + 1 in min.x - 1..max.x + 1) {
        result.add(copy(x = x + 1))
    }
    if (x - 1 in min.x - 1..max.x + 1) {
        result.add(copy(x = x - 1))
    }
    if (y + 1 in min.y - 1..max.y + 1) {
        result.add(copy(y = y + 1))
    }
    if (y - 1 in min.y - 1..max.y + 1) {
        result.add(copy(y = y - 1))
    }
    if (z + 1 in min.z + 1..max.z + 1) {
        result.add(copy(z = z + 1))
    }
    if (z - 1 in min.z - 1..max.z + 1) {
        result.add(copy(z = z - 1))
    }

    return result
}