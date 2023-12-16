package days.aoc2023

import days.Day
import util.CharArray2d
import util.Point2d
import kotlin.math.max

class Day16 : Day(2023, 16) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val tiles = CharArray2d(input)
        return calculateEnergizedTilesFromBeam(tiles, Point2d(0, 0) to Point2d.Directions.East)
    }

    fun calculatePartTwo(input: List<String>): Int {
        val tiles = CharArray2d(input)
        var max = 0
        for (x in tiles.columnIndices) {
            max = max(max, calculateEnergizedTilesFromBeam(tiles, startingBeam = Point2d(x, 0) to Point2d.Directions.South))
            max = max(max, calculateEnergizedTilesFromBeam(tiles, startingBeam = Point2d(x, tiles.maxRowIndex) to Point2d.Directions.North))
        }

        for (y in tiles.rowIndices) {
            max = max(max, calculateEnergizedTilesFromBeam(tiles, startingBeam = Point2d(0, y) to Point2d.Directions.East))
            max = max(max, calculateEnergizedTilesFromBeam(tiles, startingBeam = Point2d(tiles.maxColumnIndex, y) to Point2d.Directions.West))
        }

        return max
    }

    private fun calculateEnergizedTilesFromBeam(tiles: CharArray2d, startingBeam: Pair<Point2d, Point2d.Directions>): Int {
        val energizedTiles = mutableSetOf<Point2d>()
        val activeBeams = mutableListOf(startingBeam)
        val seenBeams = mutableSetOf<Pair<Point2d, Point2d.Directions>>()

        fun nextTileLocation(current: Point2d, direction: Point2d.Directions): Point2d? {
            return if ((current + direction.delta).isWithin(tiles)) {
                current + direction.delta
            } else {
                null
            }
        }

        fun splitBeam(beam: Pair<Point2d, Point2d.Directions>, newDirections: Set<Point2d.Directions>) {
            if (beam.second in newDirections) {
                nextTileLocation(beam.first, beam.second)?.let {
                    activeBeams.add(it to beam.second)
                }
            } else {
                newDirections.forEach { direction ->
                    nextTileLocation(beam.first, direction)?.let {
                        activeBeams.add(it to direction)
                    }
                }
            }
        }

        val rightSlant = mapOf(
            Point2d.Directions.North to Point2d.Directions.East,
            Point2d.Directions.East to Point2d.Directions.North,
            Point2d.Directions.South to Point2d.Directions.West,
            Point2d.Directions.West to Point2d.Directions.South
        )
        val leftSlant = mapOf(
            Point2d.Directions.North to Point2d.Directions.West,
            Point2d.Directions.West to Point2d.Directions.North,
            Point2d.Directions.South to Point2d.Directions.East,
            Point2d.Directions.East to Point2d.Directions.South
        )
        fun turnBeam(beam: Pair<Point2d, Point2d.Directions>, map: Map<Point2d.Directions,Point2d.Directions>) {
            map[beam.second]?.let { newDirection ->
                nextTileLocation(beam.first, newDirection)?.let {
                    activeBeams.add(it to newDirection)
                }
            }
        }

        while (activeBeams.isNotEmpty()) {
            val beam = activeBeams.removeFirst()
            if (seenBeams.contains(beam)) {
                continue
            } else {
                seenBeams.add(beam)
            }
            energizedTiles.add(beam.first)

            when (tiles[beam.first]) {
                '.' -> nextTileLocation(beam.first, beam.second)?.let {
                    activeBeams.add(it to beam.second)
                }
                '/' -> turnBeam(beam, rightSlant)
                '\\' -> turnBeam(beam, leftSlant)
                '|' -> splitBeam(beam, setOf(Point2d.Directions.North, Point2d.Directions.South))
                '-' -> splitBeam(beam, setOf(Point2d.Directions.East, Point2d.Directions.West))
            }
        }

        return energizedTiles.count()
    }

}