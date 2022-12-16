package days.aoc2022

import days.Day
import util.Point2d
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource

class Day15 : Day(2022, 15) {
    override fun partOne(): Any {
        return countCoveredBeaconPositionsForRow(inputList, 2000000)
    }

    override fun partTwo(): Any {
        return calculateTuningFrequencyOfBeacon(inputList, 4000000)
    }

    // Sensor at x=2432913, y=3069935: closest beacon is at x=2475123, y=3089709
    fun countCoveredBeaconPositionsForRow(input: List<String>, rowToAnalyze: Int): Int {
        val sensors = input.map { parseInputLine(it) }

        var minX = Int.MAX_VALUE
        var minY = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var maxY = Int.MIN_VALUE

        sensors.forEach { sensor ->
            if (sensor.location.x < minX || sensor.nearestBeacon.x < minX) {
                minX = min(sensor.location.x, sensor.nearestBeacon.x)
            }
            if (sensor.location.y < minY || sensor.nearestBeacon.y < minY) {
                minY = min(sensor.location.y, sensor.nearestBeacon.y)
            }
            if (sensor.location.x > maxX || sensor.nearestBeacon.x > maxX) {
                maxX = max(sensor.location.x, sensor.nearestBeacon.x)
            }
            if (sensor.location.y > maxY || sensor.nearestBeacon.y > maxY) {
                maxY = max(sensor.location.y, sensor.nearestBeacon.y)
            }

            minX = min(sensor.location.x - sensor.distanceToNearestBeacon, minX)
            maxX = max(sensor.location.x + sensor.distanceToNearestBeacon, maxX)
        }

        val coveredRange = mutableSetOf<Int>()
        sensors.forEach { sensor ->
            // determine if this sensor's area intersects the line we're interested in
            if (rowToAnalyze in (sensor.location.y - sensor.distanceToNearestBeacon)..(sensor.location.y + sensor.distanceToNearestBeacon)) {
                val range = sensor.distanceToNearestBeacon - abs(sensor.location.y - rowToAnalyze)
                coveredRange.addAll((sensor.location.x - range)..(sensor.location.x + range))
            }
        }

        return coveredRange.size - sensors.map { it.nearestBeacon }.distinct().count { it.y == rowToAnalyze && it.x in coveredRange }
    }

    private fun parseInputLine(line: String): Sensor {
        return Regex("Sensor at x=(\\d+), y=(\\d+): closest beacon is at x=(-?)(\\d+), y=(-?)(\\d+)")
            .matchEntire(line)?.destructured?.let { (x, y, signX, beaconX, signY, beaconY) ->
                Sensor(Point2d(x.toInt(), y.toInt()),
                    Point2d((if (signX == "-") beaconX.toInt().unaryMinus() else beaconX.toInt()),
                        (if (signY == "-") beaconY.toInt().unaryMinus() else beaconY.toInt())))
            } ?: throw IllegalStateException()
    }

    fun calculateTuningFrequencyOfBeacon(input: List<String>, maxExtent: Int): Long {
        val sensors = input.map { parseInputLine(it) }

        sensors.forEach { sensor ->
            for (x in 0..sensor.distanceToNearestBeacon + 1) {
                val pointsToTest = listOf(
                    Point2d(sensor.location.x + x, sensor.location.y + (sensor.distanceToNearestBeacon + 1) - x),
                    Point2d(sensor.location.x - x, sensor.location.y + (sensor.distanceToNearestBeacon + 1) - x),
                    Point2d(sensor.location.x + x, sensor.location.y - (sensor.distanceToNearestBeacon + 1) - x),
                    Point2d(sensor.location.x - x, sensor.location.y - (sensor.distanceToNearestBeacon + 1) - x),
                )

                pointsToTest.filter { it.x in 0..maxExtent && it.y in 0..maxExtent }.forEach { point ->
                    if (!sensors.any {
                        point.distanceTo(it.location) <= it.distanceToNearestBeacon
                    }) {
                        return point.x * 4000000L + point.y
                    }

                }

            }
        }

        throw IllegalStateException()
    }

    data class Sensor(val location: Point2d, val nearestBeacon: Point2d) {
        val distanceToNearestBeacon = location.distanceTo(nearestBeacon)
    }
}