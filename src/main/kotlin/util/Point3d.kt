package util

import kotlin.math.abs

data class Point3d(val x: Int, val y: Int, val z: Int) {
    fun distanceTo(other: Point3d): Int {
        return abs(other.x - x) + abs(other.y - y) + abs(other.z - z)
    }
}

data class Point3dl(val x: Long, val y: Long, val z: Long) {
    fun distanceTo(other: Point3dl): Long {
        return abs(other.x - x) + abs(other.y - y) + abs(other.z - z)
    }
}