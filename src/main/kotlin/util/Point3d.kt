package util

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class Point3d(val x: Int, val y: Int, val z: Int) {
    fun distanceTo(other: Point3d): Int {
        return abs(other.x - x) + abs(other.y - y) + abs(other.z - z)
    }

    fun euclidianDistanceTo(other: Point3d): Double {
        return sqrt(
            (other.x - x).toDouble().pow(2.0) +
                    (other.y - y).toDouble().pow(2.0) +
                    (other.z - z).toDouble().pow(2.0)
        )
    }

    fun neighbors(min: Point3d, max: Point3d): List<Point3d> {
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
}

data class Point3dl(val x: Long, val y: Long, val z: Long) {
    fun distanceTo(other: Point3dl): Long {
        return abs(other.x - x) + abs(other.y - y) + abs(other.z - z)
    }
}